package donut;

import donut.spril.Instruction;
import donut.spril.Operator;
import donut.spril.Program;
import donut.spril.Reg;
import donut.spril.instructions.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ron on 23-6-2016.
 *
 * Generates Spril instructions from parsed Donut code.
 * Supports concurrency.
 *
 * All visitor methods return the line number of the first instruction. This makes it easier to know where to jump/branch to.
 */
public class GeneratorIII extends DonutBaseVisitor<Integer> {

    /** Boolean representation in Spril instructions */
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    /** Number of threads that do work during concurrent blocks */
    public static final int THREADS = 3;
    /** Memory sizes (4 bytes per location) */
    public static final int LOCALMEMSIZE = 16;
    public static final int SHAREDMEMSIZE = 16;
    /** Register with zero constant */
    public static final Reg ZEROREG = new Reg("(reg0)");

    /** Result of the checker phase */
    private CheckerResultII result;
    /** Counter for creating fresh registers */
    private int regCount;
    /** Line counter (used for jumps) */
    private int lineCount;
    /** Maps expression values to registers */
    private ParseTreeProperty<Reg> registers;
    /** Reference to the main program */
    private Program program;
    /** List of all programs (main and threads */
    private List<Program> programs;

    /**
     * Generate a list of programs. The main program is stored at index 0. The rest of the programs store threads
     * @param tree -- The parsed Donut code that will be converted to Spril instructions
     * @param result -- Result from the checker phase
     */
    public List<Program> generate(ParseTree tree, CheckerResultII result)   {
        this.program = new Program();
        this.programs = new ArrayList<>();
        this.registers = new ParseTreeProperty<>();

        this.programs.add(program);             // Add main
        for (int i = 0; i < THREADS; i++)  {
            programs.add(new Program());        // Add potential threads
        }
        this.result = result;
        this.regCount = 1;                      // Reg 0 cannot be used
        this.lineCount = 0;

        tree.accept(this);                      // Visit the tree

        program.add(new Read(ZEROREG));         // Read/Receive makes sure the write buffer is empty at the end of main
        program.add(new Receive(ZEROREG));

        for (Program p : programs)  {           // Append EndProg to all programs
            p.add(new EndProg());
        }

        return programs;
    }

    /*
        Visit methods
     */

    /** Visit program - Return first line number */
    @Override
    public Integer visitProgram(DonutParser.ProgramContext ctx) {
        int begin = lineCount;
        visitChildren(ctx);
        return begin;
    }

    /** Visit block - Return first line number */
    @Override
    public Integer visitBlock(DonutParser.BlockContext ctx) {
        int begin = lineCount;
        this.visitChildren(ctx);
        return begin;
    }

    /** Visit concurrent block - Divides the statements in the block into partitions that are distributed over the threads.
     *  Generate threads to execute the statements
     *  Add instructions to the main program that will start all threads at the start of the block
     *  and will wait at the end of the block until all threads are finished */
    @Override
    public Integer visitConcurrentBlock(DonutParser.ConcurrentBlockContext ctx) {
        int begin = lineCount;
        // Divide statements between threads
        List<ParseTree> stats = ctx.children.subList(1, ctx.getChildCount() - 1);               // Obtain statements in block (1st and last children are braces)
        int partitionSize = (int) Math.ceil((double) stats.size()/(double) THREADS);            // Determine partition size. Round up to ensure the amount of partitions will always equal the amount of threads
        List<List<ParseTree>> partitions = new ArrayList<>();
        int partitionStart = 0;
        for (int threadID = 0; threadID < THREADS && threadID < stats.size(); threadID++)  {    // Take a sublist of the statements
            if (stats.size() - partitionStart >= partitionSize)   {
                partitions.add(stats.subList(partitionStart, partitionStart + partitionSize));
                partitionStart += partitionSize;
            } else {
                partitions.add(stats.subList(partitionStart, stats.size()));
            }
        }

        // Make threads
        for (int threadID = 0; threadID < THREADS && threadID < stats.size(); threadID++)  {
            List<ParseTree> partition = partitions.get(threadID);                               // Obtain the statements to be executed by this thread
            ThreadGenerator generator = new ThreadGenerator(threadID);                          // Create a generator that will make the thread

            Program thread = generator.generate(partition, result, programs.get(threadID + 1)); // Generate the thread
            programs.set(threadID + 1, thread);                                                 // Set the updated program (at index threadID + 1 because the 1st program is the main program)

            emit(new TestAndSetAI(threadID));                                                   // Start thread by setting the reserved space in memory to 1
            emit(new Receive(reg(ctx)));                                                        // Make sure the thread is started before continuing
            emit(new BranchI(reg(ctx), 2, false));                                              // Thread was started     -> continue by branching over the jump instruction
            emit(new JumpI(-3, false));                                                         // Thread was not started -> Try again
        }

        // Join all threads
        for (int threadID = 0; threadID < THREADS && threadID < stats.size(); threadID++)  {    // For all threads:
            emit(new ReadAI(threadID));                                                         // Read in shared memory if the thread has indicated that it is finished
            emit(new Receive(reg(ctx)));                                                        // Receive reply (0 means thread is finished)
            emit(new BranchI(reg(ctx), -2, false));                                             // If not -> read again
        }

        return begin;
    }

    /*
        Stat
     */

    /** Visit assign statement - if the variable is global, write to shared memory. Otherwise write to local memory */
    @Override
    public Integer visitAssStat(DonutParser.AssStatContext ctx) {
        int begin = visit(ctx.expr());
        Reg exprReg = reg(ctx.expr());
        int offset = offset(ctx.ID());
        if (shared(ctx.ID()))   {
            emit(new WriteAI(exprReg, offset));
        } else {
            emit(new StoreAI(exprReg, offset));
        }
        return begin;
    }

    /** Visit if statement - Makes sure the program jumps correctly for if-statements */
    @Override
    public Integer visitIfStat(DonutParser.IfStatContext ctx) {
        int begin = visit(ctx.expr());
        Reg r_cmp = registers.get(ctx.expr());
        if (ctx.ELSE() == null)   {
            Instruction branch = emit(new BranchI(r_cmp, -1, true));            // Branch to then (line number will be determined later)
            Instruction jump = emit(new JumpI( -1, true));                      // Jump to end

            int thenLine = visit(ctx.block(0));                                 // Generate code in block
            this.program.replace(branch, new BranchI(r_cmp, thenLine, true));   // Now that the starting line of the block is known, update the branch instruction
            this.program.replace(jump, new JumpI(lineCount, true));             // Do the same for the jump to end
            emit(new Nop());                                                    // Create a nop instruction so the program can jump to end
        } else {
            Instruction branch = emit(new BranchI(r_cmp, -1, true));            // Branch to then
            Instruction jump = emit(new JumpI(-1, true));                       // Jump to else
            Instruction endJump = this.emit(new JumpI(-1, true));               // Jump to end
            int thenLine = visit(ctx.block(0));                                 // Generate code in the 'then' block
            int elseLine = visit(ctx.block(1));                                 // Generate code in the 'else' block
            this.program.replace(branch, new BranchI(r_cmp, thenLine, true));   // Update the branch/jump instructions with the right line numbers
            this.program.replace(jump, new JumpI(elseLine, true));
            this.program.replace(endJump, new JumpI(lineCount, true));
            emit(new Nop());
        }
        return begin;
    }

    /** Visit while statement - Makes sure the program jumps correctly for while-statements */
    @Override
    public Integer visitWhileStat(DonutParser.WhileStatContext ctx) {
        int begin = visit(ctx.expr());                                  // Generate the condition code
        Reg r_cmp = reg(ctx.expr());
        Instruction branch = emit(new BranchI(r_cmp, -1, true));        // Branch to block if the condition is met
        Instruction jump = emit(new JumpI(-1, false));                  // Otherwise jump to end
        visit(ctx.block());                                             // Generate code in block
        emit(new JumpI(begin, true));                                   // After executing the block, jump to condition again
        program.replace(branch, new BranchI(r_cmp, 2, false));          // Update the branch/jump instructions with the right line numbers
        program.replace(jump, new JumpI(lineCount, true));
        emit(new Nop());
        return begin;
    }

    /** Visit declaration statement - First checks whether the programmer directly assigns a value to the variable
     *  otherwise assigns the default value to the created variable.
     *  Also checks whether the variable is declared globally or locally and stores it into shared or local memory, respectively */
    @Override
    public Integer visitDeclStat(DonutParser.DeclStatContext ctx) {
        int begin;
        if (ctx.ASSIGN() != null) {                                         // Assign context is present -> Value is assigned
            begin = visit(ctx.expr());
            Reg resultExpr = reg(ctx.expr());
            if (ctx.GLOBAL() != null)   {
                emit(new WriteAI(resultExpr, offset(ctx.ID())));
            } else {
                emit(new StoreAI(resultExpr, offset(ctx.ID())));
            }
        } else {                                                            // No assign context -> Set default value
            begin = lineCount;
            if (ctx.GLOBAL() != null)   {
                emit(new WriteAI(ZEROREG, offset(ctx.ID())));
            } else {
                emit(new StoreAI(ZEROREG, offset(ctx.ID())));
            }
        }
        return begin;
    }

    /*
        Expr
     */

    /** Visit parenthesis expression - Assign the register used by the inner expression */
    @Override
    public Integer visitParExpr(DonutParser.ParExprContext ctx) {
        int begin = visitChildren(ctx);
        this.registers.put(ctx, this.registers.get(ctx.expr()));
        return begin;
    }

    /** Visit number expression - Load the value of the number in a register */
    @Override
    public Integer visitNumExpr(DonutParser.NumExprContext ctx) {
        emit(new LoadI(Integer.parseInt(ctx.NUM().getText()), reg(ctx)));
        return lineCount - 1;
    }

    @Override
    public Integer visitCharExpr(DonutParser.CharExprContext ctx) {
        return super.visitCharExpr(ctx);    // TODO
    }

    @Override
    public Integer visitArrayExpr(DonutParser.ArrayExprContext ctx) {
        return super.visitArrayExpr(ctx);   // TODO
    }

    /** Visit true expression - Load the Spril value for true in a register */
    @Override
    public Integer visitTrueExpr(DonutParser.TrueExprContext ctx) {
        emit(new LoadI(TRUE, reg(ctx)));
        return lineCount - 1;
    }

    /** Visit false expression - Load the Spril value for false in a register */
    @Override
    public Integer visitFalseExpr(DonutParser.FalseExprContext ctx) {
        emit(new LoadI(FALSE, reg(ctx)));
        return lineCount - 1;
    }

    /** Visit ID expression - Obtain the offset of the variable from the checker result.
     *  Store the value read from memory in a register */
    @Override
    public Integer visitIdExpr(DonutParser.IdExprContext ctx) {
        int begin = lineCount;
        if (shared(ctx.ID())) {
            emit(new ReadAI(offset(ctx.ID())));
            emit(new Receive(reg(ctx)));
        } else {
            emit(new LoadAI(offset(ctx.ID()), reg(ctx)));
        }
        return begin;
    }

    /*
        -- Operations
     */

    /** Visit multiplication expression - Visit the two expressions first and compute the result */
    @Override
    public Integer visitMultExpr(DonutParser.MultExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));
        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));
        emit(new Compute(Operator.MUL, r0, r1, reg(ctx)));
        return begin;
    }

    /** Visit subtraction expression - Visit the two expressions first and compute the result */
    @Override
    public Integer visitMinusExpr(DonutParser.MinusExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));
        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));
        emit(new Compute(Operator.SUB, r0, r1, reg(ctx)));
        return begin;
    }

    /** Visit addition expression - Visit the two expressions first and compute the result */
    @Override
    public Integer visitPlusExpr(DonutParser.PlusExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));
        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));
        emit(new Compute(Operator.ADD, r0, r1, reg(ctx)));
        return begin;
    }

    /** Visit comparison expression - Visit the two expressions first and compute the result using the correct operator */
    @Override
    public Integer visitCompExpr(DonutParser.CompExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        if (ctx.compOperator().EQUALS() != null)   {
            emit(new Compute(Operator.EQUAL, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().NOTEQUALS() != null)    {
            emit(new Compute(Operator.NEQ, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().GT() != null)    {
            emit(new Compute(Operator.GT, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().GE() != null)    {
            emit(new Compute(Operator.GTE, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().LT() != null)    {
            emit(new Compute(Operator.LT, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().LE() != null)    {
            emit(new Compute(Operator.LTE, r0, r1, reg(ctx)));
        }
        return begin;
    }

    /** Visit division expression - Visit the two expressions first and compute the result */
    @Override
    public Integer visitDivExpr(DonutParser.DivExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));
        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));
        emit(new Compute(Operator.DIV, r0, r1, reg(ctx))); // Div operation no longer supported by Sprockell
        return begin;
    }

    @Override
    public Integer visitPowExpr(DonutParser.PowExprContext ctx) {
        return super.visitPowExpr(ctx);         // TODO
    }

    /** Visit boolean expression - Visit the two expressions first and compute the result using the correct operator */
    @Override
    public Integer visitBoolExpr(DonutParser.BoolExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        if (ctx.boolOperator().AND() != null)   {
            emit(new Compute(Operator.AND, r0, r1, reg(ctx)));
        } else if (ctx.boolOperator().OR() != null) {
            emit(new Compute(Operator.OR, r0, r1, reg(ctx)));
        } else if (ctx.boolOperator().XOR() != null) {
            emit(new Compute(Operator.XOR, r0, r1, reg(ctx)));
        }
        return begin;
    }

    /** Visit prefix expression - Visit the expression first, then invert it */
    @Override
    public Integer visitPrfExpr(DonutParser.PrfExprContext ctx) {
        int begin = visit(ctx.expr());
        visit(ctx.prfOperator());

        if (ctx.prfOperator().MINUS() != null)   {                                      // Minus prefix
            Reg reg = registers.get(ctx.expr());                                        // Invert by multiplying with -1
            Reg temp = reg(ctx);
            setReg(ctx, reg);
            emit(new LoadI(-1, temp));
            emit(new Compute(Operator.MUL, reg, temp, reg));
        } else {                                                                        // Not prefix
            Reg temp = reg(ctx);                                                        // Invert by subtracting from one
            emit(new LoadI(1, temp));
            Reg reg = registers.get(ctx.expr());
            emit(new Compute(Operator.SUB, temp, reg, reg));
            setReg(ctx, reg);
        }
        return  begin;
    }

    /*
        Help functions
     */

    /** Add the instruction to the main program. Update the line counter */
    private Instruction emit(Instruction instr) {
        program.add(instr);
        lineCount++;
        return instr;
    }

    /** Obtain the node's offset as determined by the checker phase */
    private int offset(ParseTree node)  {
        return this.result.getOffset(node);
    }

    /** Indicates whether the node is stored globally or locally */
    private boolean shared(ParseTree node) { return this.result.isShared(node); }

    /** Returns a register for a given parse tree node,
     * creating a fresh register if there is none for that node. */
    private Reg reg(ParseTree node) {
        Reg result = this.registers.get(node);
        if (result == null) {
            result = new Reg("(reg" + this.regCount + ")");
            this.registers.put(node, result);
            this.regCount++;
        }
        return result;
    }

    /** Set the register for this node */
    private void setReg(ParseTree node, Reg reg) {
        this.registers.put(node, reg);
    }


}
