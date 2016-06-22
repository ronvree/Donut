package donut;

import donut.spril.Instruction;
import donut.spril.Operator;
import donut.spril.Program;
import donut.spril.Reg;
import donut.spril.instructions.Compute;
import donut.spril.instructions.LoadAI;
import donut.spril.instructions.LoadI;
import org.antlr.v4.runtime.tree.*;

/**
 * Created by Ron on 21-6-2016.
 *
 * Generates Spril instructions from donut code
 */
public class Generator extends DonutBaseVisitor<Instruction> {

    private static final int TRUE = 1;
    private static final int FALSE = 0;

    /**
     * Result from the checker phase
     */
    private CheckerResult result;


    private ParseTreeProperty<LabelEntry> labels; // TODO -- do we need this?

    /**
     * Used for making new registers
     */
    private int regCount;
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
        this.labels = new ParseTreeProperty<>();
        this.registers = new ParseTreeProperty<>();
        this.regCount = 0;
        tree.accept(this);
//        this.visit(tree);
        return program;
    }


    @Override
    public Instruction visitProgram(DonutParser.ProgramContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitBlock(DonutParser.BlockContext ctx) {
        return visitChildren(ctx);
    }

    /*
        Stat
     */

    @Override
    public Instruction visitAssStat(DonutParser.AssStatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitIfStat(DonutParser.IfStatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitWhileStat(DonutParser.WhileStatContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Instruction visitDeclStat(DonutParser.DeclStatContext ctx) {
        return visitChildren(ctx);
    }


    /*
        Expr
     */


    @Override
    public Instruction visitParExpr(DonutParser.ParExprContext ctx) {
        Instruction instr = visitChildren(ctx);
        this.registers.put(ctx, this.registers.get(ctx.expr()));
        return instr;
    }

    @Override
    public Instruction visitIdExpr(DonutParser.IdExprContext ctx) {
        return emit(new LoadAI(offset(ctx.ID()), reg(ctx)));
    }

    @Override
    public Instruction visitNumExpr(DonutParser.NumExprContext ctx) {
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
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.MUL, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitMinusExpr(DonutParser.MinusExprContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.SUB, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitPlusExpr(DonutParser.PlusExprContext ctx) {
        visit(ctx.expr(0));
        visit(ctx.expr(1));

        Reg r0 = reg(ctx.expr(0));
        Reg r1 = reg(ctx.expr(1));

        return emit(new Compute(Operator.ADD, r0, r1, reg(ctx)));
    }

    @Override
    public Instruction visitCompExpr(DonutParser.CompExprContext ctx) {
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
            result = new Reg("(Reg " + this.regCount + ")");
            this.registers.put(node, result);
            this.regCount++;
        }
        return result;
    }

    private void setReg(ParseTree node, Reg reg) {
        this.registers.put(node, reg);
    }

    private class LabelEntry    {
        private int line;
        private boolean absolute;

        public LabelEntry(int line, boolean absolute)   {
            this.line = line;
            this.absolute = absolute;
        }

    }

}
