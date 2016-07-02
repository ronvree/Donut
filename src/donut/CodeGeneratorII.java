package donut;

import donut.checkers.CheckerResultII;
import donut.DonutBaseVisitor;
import donut.DonutParser;
import donut.spril.Instruction;
import donut.spril.Operator;
import donut.spril.Program;
import donut.spril.Reg;
import donut.spril.instructions.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * We implemented two generator types. One that generates the main program and one that can generate threads. To ensure
 * both generate Spril code the same way they both extend this class.
 *
 * All visitor methods return the line number of the first instruction. This makes it easier to know where to jump/branch to.
 */
public abstract class CodeGeneratorII extends DonutBaseVisitor<Integer> {

    /** Boolean representation in Spril instructions */
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    /** Number of threads that do work during concurrent blocks */
    public static final int THREADS = 2;
    /** Memory sizes (4 bytes per location) */
    public static final int LOCALMEMSIZE = 16;
    public static final int SHAREDMEMSIZE = 16;
    public static final int REGBANKSIZE = 23;
    /** Register with zero constant */
    public static final Reg ZEROREG = new Reg("(reg0)");

    /** Result of the checker phase */
    private CheckerResultII result;
    /** Counter for creating fresh registers */
    private RegUlator regpool;
    /** Line counter (used for jumps) */
    private int lineCount;
    /** Maps expression values to registers */
    private ParseTreeProperty<Reg> registers;
    /** Reference to the main program */
    private Program program;

    public CodeGeneratorII()    {
        this.regpool = new RegUlator(REGBANKSIZE - 2);
        this.registers = new ParseTreeProperty<>();
    }

    /*
        Visitor methods
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
        release(exprReg);
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
        release(r_cmp);
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
        release(r_cmp);
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
            release(resultExpr);
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

    /** Visit multiplication expression - Visit the two expressions first, determine the operator and compute the result */
    @Override
    public Integer visitMultExpr(DonutParser.MultExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));
        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));
        if (ctx.multop().MULT() != null)   {
            emit(new Compute(Operator.MUL, r0, r1, r0));
        } else {
            emit(new Compute(Operator.DIV, r0, r1, r0)); // Div operation no longer supported by Sprockell
        }
        release(r1);
        return begin;
    }

    /** Visit addition expression - Visit the two expressions first, determine the operator and compute the result */
    @Override
    public Integer visitPlusExpr(DonutParser.PlusExprContext ctx) {
        int begin = visit(ctx.expr(0));
        visit(ctx.expr(1));
        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));
        if (ctx.plusop().PLUS() != null)   {
            emit(new Compute(Operator.ADD, r0, r1, r0));
        } else {
            emit(new Compute(Operator.SUB, r0, r1, r0));
        }
        release(r1);
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
            emit(new Compute(Operator.EQUAL, r0, r1, r0));
        } else if (ctx.compOperator().NOTEQUALS() != null)    {
            emit(new Compute(Operator.NEQ, r0, r1, r0));
        } else if (ctx.compOperator().GT() != null)    {
            emit(new Compute(Operator.GT, r0, r1, r0));
        } else if (ctx.compOperator().GE() != null)    {
            emit(new Compute(Operator.GTE, r0, r1, r0));
        } else if (ctx.compOperator().LT() != null)    {
            emit(new Compute(Operator.LT, r0, r1, r0));
        } else if (ctx.compOperator().LE() != null)    {
            emit(new Compute(Operator.LTE, r0, r1, r0));
        }
        release(r1);
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
            emit(new Compute(Operator.AND, r0, r1, r0));
        } else if (ctx.boolOperator().OR() != null) {
            emit(new Compute(Operator.OR, r0, r1, r0));
        } else if (ctx.boolOperator().XOR() != null) {
            emit(new Compute(Operator.XOR, r0, r1, r0));
        }
        release(r1);
        return begin;
    }

    /** Visit prefix expression - Visit the expression first, then invert it */
    @Override
    public Integer visitPrfExpr(DonutParser.PrfExprContext ctx) {
        int begin = visit(ctx.expr());
        visit(ctx.prfOperator());

        if (ctx.prfOperator().MINUS() != null)   {                                      // Minus prefix
            Reg reg = registers.get(ctx.expr());                                        // Invert by multiplying with -1
            setReg(ctx, reg);
            emit(new ComputeI(Operator.MUL, -1, reg, reg));
        } else {                                                                        // Not prefix
            Reg reg = registers.get(ctx.expr());
            setReg(ctx, reg);
            emit(new ComputeI(Operator.SUB, 1, reg, reg));
        }
        return  begin;
    }

    /*
        Help functions
     */

    /** Add the instruction to the main program. Update the line counter */
    protected Instruction emit(Instruction instr) {
        program.add(instr);
        lineCount++;
        return instr;
    }

    /** Obtain the node's offset as determined by the checker phase */
    protected int offset(ParseTree node)  {
        return this.result.getOffset(node);
    }

    /** Indicates whether the node is stored globally or locally */
    protected boolean shared(ParseTree node) { return this.result.isShared(node); }

    /** Returns a register for a given parse tree node,
     * creating a fresh register if there is none for that node. */
    protected Reg reg(ParseTree node) {
        Reg result = this.registers.get(node);
        if (result == null) {
            try {
                result = regpool.get();
            } catch (RegUlator.NoRegException e) {
                System.out.println("Dude, make your expressions less complex!");
            }
            this.registers.put(node, result);
        }
        return result;
    }

    protected void setReg(ParseTree node, Reg reg) {
        this.registers.put(node, reg);
    }

    /** Indicates that the register can be reused */
    protected void release(Reg reg) { this.regpool.add(reg); }

    /*
        Getters and setters
     */

    public RegUlator getRegpool() {
        return regpool;
    }

    protected CheckerResultII getResult() {
        return result;
    }

    protected void setResult(CheckerResultII result) {
        this.result = result;
    }

    protected int getLineCount() {
        return lineCount;
    }

    protected void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    protected ParseTreeProperty<Reg> getRegisters() {
        return registers;
    }

    protected void setRegisters(ParseTreeProperty<Reg> registers) {
        this.registers = registers;
    }

    protected Program getProgram() {
        return program;
    }

    protected void setProgram(Program program) {
        this.program = program;
    }


}
