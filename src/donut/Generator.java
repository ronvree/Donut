package donut;

import donut.spril.Instruction;
import donut.spril.Operator;
import donut.spril.Program;
import donut.spril.Reg;
import donut.spril.instructions.BranchI;
import donut.spril.instructions.Compute;
import donut.spril.instructions.LoadAI;
import donut.spril.instructions.LoadI;
import donut.spril.instructions.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Created by Ron on 21-6-2016.
 *
 * Generates Spril instructions from donut code
 */
public class Generator extends DonutBaseVisitor<Instruction> {

    private static final int TRUE = 1;
    private static final int FALSE = 0;
    private static final Reg alwaysZero = new Reg("(reg0)");

    /**
     * Result from the checker phase
     */
    private CheckerResult result;
    /**
     * Used for making new registers
     */
    private int regCount;
    /**
     * Line counter (used for jump instructions)
     */
    private int lineCount;
    /**
     * Stores starting lines of instructions (for jumps)
     */
    private ParseTreeProperty<Integer> jumpLines;
    /**
     * Bind registers to nodes
     */
    private ParseTreeProperty<Reg> registers;
    /**
     * Program to build
     */
    private Program program;

    public Program generate(ParseTree tree, CheckerResult result)   {
        this.program = new Program();
        this.result = result;
        this.jumpLines = new ParseTreeProperty<>();
        this.registers = new ParseTreeProperty<>();
        this.regCount = 1;
        this.lineCount = 0;
        tree.accept(this);
        return program;
    }


    @Override
    public Instruction visitProgram(DonutParser.ProgramContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitBlock(DonutParser.BlockContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        return visitChildren(ctx);
    }

    /*
        Stat
     */

    @Override
    public Instruction visitAssStat(DonutParser.AssStatContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        Instruction first = visit(ctx.expr());
        Reg exprReg = reg(ctx.expr());
        int offset = offset(ctx.ID());
        emit(new StoreAI(exprReg, offset));
        return first;
    }

    @Override
    public Instruction visitIfStat(DonutParser.IfStatContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        Instruction cmp = visit(ctx.expr());
        Reg r_cmp = registers.get(ctx.expr());
        if (ctx.ELSE() == null)   {
            Instruction branch = emit(new BranchI(r_cmp, -1, true)); // Branch to then
            Instruction jump = emit(new JumpI( -1, true));           // Jump to end
            visit(ctx.block(0));
            int thenLine = this.jumpLines.get(ctx.block(0));
            this.program.replace(branch, new BranchI(r_cmp, thenLine, true));
            this.program.replace(jump, new JumpI(lineCount, true));
            emit(new Nop());
        } else {
            Instruction branch = emit(new BranchI(r_cmp, -1, true)); // Branch to then
            Instruction jump = emit(new JumpI(-1, true)); // Jump to else
            visit(ctx.block(0));
            Instruction endJump = this.emit(new JumpI(-1, true)); // Jump to end
            visit(ctx.block(1));
            int thenLine = this.jumpLines.get(ctx.block(0));
            int elseLine = this.jumpLines.get(ctx.block(1));
            this.program.replace(branch, new BranchI(r_cmp, thenLine, true));
            this.program.replace(jump, new JumpI(elseLine, true));
            this.program.replace(endJump, new JumpI(lineCount, true));
            emit(new Nop());
        }
        return cmp;
    }

    @Override
    public Instruction visitWhileStat(DonutParser.WhileStatContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        int branchLine = lineCount;
        Instruction first = visit(ctx.expr());
        Reg r_cmp = reg(ctx.expr());
        Instruction branch = emit(new BranchI(r_cmp, -1, true));
        Instruction jump = emit(new JumpI(-1, false));
        int afterJump = lineCount;
        visit(ctx.block());
        emit(new JumpI(branchLine, false));
        int afterBranch = lineCount;
        emit(new Nop());
        program.replace(branch, new BranchI(r_cmp, afterJump, false));
        program.replace(jump, new JumpI(afterBranch, false));
        return first;
    }

    @Override
    public Instruction visitDeclStat(DonutParser.DeclStatContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        Instruction first;
        if (ctx.getChildCount() > 3) {
            first = visit(ctx.expr());
            Reg resultExpr = reg(ctx.expr());
            emit(new StoreAI(resultExpr, offset(ctx.ID())));
        } else {
            first = emit(new StoreAI(alwaysZero, offset(ctx.ID())));
        }
        return first;
    }


    /*
        Expr
     */


    @Override
    public Instruction visitParExpr(DonutParser.ParExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        Instruction instr = visitChildren(ctx);
        this.registers.put(ctx, this.registers.get(ctx.expr()));
        return instr;
    }

    @Override
    public Instruction visitIdExpr(DonutParser.IdExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        return emit(new LoadAI(offset(ctx.ID()), reg(ctx)));
    }

    @Override
    public Instruction visitNumExpr(DonutParser.NumExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        return emit(new LoadI(Integer.parseInt(ctx.NUM().getText()), reg(ctx)));
    }

    @Override
    public Instruction visitCharExpr(DonutParser.CharExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitArrayExpr(DonutParser.ArrayExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitTrueExpr(DonutParser.TrueExprContext ctx) {
        return emit(new LoadI(TRUE, reg(ctx)));
    }

    @Override
    public Instruction visitFalseExpr(DonutParser.FalseExprContext ctx) {
        return emit(new LoadI(FALSE, reg(ctx)));
    }

    /*
        -- Operations
     */

    @Override
    public Instruction visitMultExpr(DonutParser.MultExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.MUL, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitMinusExpr(DonutParser.MinusExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.SUB, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitPlusExpr(DonutParser.PlusExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.ADD, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitCompExpr(DonutParser.CompExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        if (ctx.compOperator().EQUALS() != null)   {
            return emit(new Compute(Operator.EQUAL, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().NOTEQUALS() != null)    {
            return emit(new Compute(Operator.NEQ, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().GT() != null)    {
            return emit(new Compute(Operator.GT, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().GE() != null)    {
            return emit(new Compute(Operator.GTE, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().LT() != null)    {
            return emit(new Compute(Operator.LT, r0, r1, reg(ctx)));
        } else if (ctx.compOperator().LE() != null)    {
            return emit(new Compute(Operator.LTE, r0, r1, reg(ctx)));
        } else {
            return null;
        }
    }

    @Override
    public Instruction visitDivExpr(DonutParser.DivExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.DIV, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitPowExpr(DonutParser.PowExprContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitBoolExpr(DonutParser.BoolExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        if (ctx.boolOperator().AND() != null)   {
            return emit(new Compute(Operator.AND, r0, r1, reg(ctx)));
        } else if (ctx.boolOperator().OR() != null) {
            return emit(new Compute(Operator.OR, r0, r1, reg(ctx)));
        } else if (ctx.boolOperator().XOR() != null) {
            return emit(new Compute(Operator.XOR, r0, r1, reg(ctx)));
        } else {
            System.out.println("NO SUCH OPERATION IN GENERATOR.VISITBOOLEXPR");
            return null;
        }
    }

    @Override
    public Instruction visitPrfExpr(DonutParser.PrfExprContext ctx) {
        this.jumpLines.put(ctx, lineCount);
        visit(ctx.expr());
        visit(ctx.prfOperator());

        if (ctx.prfOperator().MINUS() != null)   {
            Reg reg = registers.get(ctx.expr());
            setReg(ctx, reg);
            Reg temp = reg(ctx);
            emit(new LoadI(-1, temp));
            return emit(new Compute(Operator.MUL, reg, temp, reg));
        } else {
            Reg temp = reg(ctx);
            emit(new LoadI(1, temp));
            Reg reg = registers.get(ctx.expr());
            Instruction instr = emit(new Compute(Operator.SUB, temp, reg, reg));
            setReg(ctx, reg);
            return instr;
        }
    }


    /*
        Other
     */


    private Instruction emit(Instruction instr) {
        program.add(instr);
        lineCount++;
        return instr;
    }

    private int offset(ParseTree node)  {
        return this.result.getOffset(node);
    }

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

    private void setReg(ParseTree node, Reg reg) {
        this.registers.put(node, reg);
    }


}
