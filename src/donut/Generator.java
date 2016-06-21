package donut;

import donut.spril.Instruction;
import org.antlr.v4.runtime.tree.*;

/**
 * Created by Ron on 21-6-2016.
 */
public class Generator implements DonutVisitor<Instruction> {


    private CheckerResult result;

    private ParseTreeProperty<LabelEntry> labels;


    @Override
    public Instruction visitProgram(DonutParser.ProgramContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBlock(DonutParser.BlockContext ctx) {
        return null;
    }

    @Override
    public Instruction visitAssStat(DonutParser.AssStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitIfStat(DonutParser.IfStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitWhileStat(DonutParser.WhileStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitDeclStat(DonutParser.DeclStatContext ctx) {
        return null;
    }

    @Override
    public Instruction visitCharExpr(DonutParser.CharExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitArrayExpr(DonutParser.ArrayExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitTrueExpr(DonutParser.TrueExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitMultExpr(DonutParser.MultExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitMinusExpr(DonutParser.MinusExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitNumExpr(DonutParser.NumExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPlusExpr(DonutParser.PlusExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitParExpr(DonutParser.ParExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitCompExpr(DonutParser.CompExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPrfExpr(DonutParser.PrfExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitDivExpr(DonutParser.DivExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitFalseExpr(DonutParser.FalseExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPowExpr(DonutParser.PowExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBoolExpr(DonutParser.BoolExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitIdExpr(DonutParser.IdExprContext ctx) {
        return null;
    }

    @Override
    public Instruction visitType(DonutParser.TypeContext ctx) {
        return null;
    }

    @Override
    public Instruction visitBoolOperator(DonutParser.BoolOperatorContext ctx) {
        return null;
    }

    @Override
    public Instruction visitCompOperator(DonutParser.CompOperatorContext ctx) {
        return null;
    }

    @Override
    public Instruction visitPrfOperator(DonutParser.PrfOperatorContext ctx) {
        return null;
    }

    @Override
    public Instruction visit(ParseTree parseTree) {
        return null;
    }

    @Override
    public Instruction visitChildren(RuleNode ruleNode) {
        return null;
    }

    @Override
    public Instruction visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public Instruction visitErrorNode(ErrorNode errorNode) {
        return null;
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
