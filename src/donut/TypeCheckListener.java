package donut;

import donut.errors.TypeError;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * Created by Ron on 19-6-2016.
 */
public class TypeCheckListener implements DonutListener {

    private ParseTreeProperty<Type> types;
    private List<TypeError> errors;

    @Override
    public void enterProgram(DonutParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(DonutParser.ProgramContext ctx) {

    }

    @Override
    public void enterBlock(DonutParser.BlockContext ctx) {

    }

    @Override
    public void exitBlock(DonutParser.BlockContext ctx) {

    }

    @Override
    public void enterAssStat(DonutParser.AssStatContext ctx) {

    }

    @Override
    public void exitAssStat(DonutParser.AssStatContext ctx) {

    }

    @Override
    public void enterIfStat(DonutParser.IfStatContext ctx) {

    }

    @Override
    public void exitIfStat(DonutParser.IfStatContext ctx) {

    }

    @Override
    public void enterWhileStat(DonutParser.WhileStatContext ctx) {

    }

    @Override
    public void exitWhileStat(DonutParser.WhileStatContext ctx) {

    }

    @Override
    public void enterDeclStat(DonutParser.DeclStatContext ctx) {

    }

    @Override
    public void exitDeclStat(DonutParser.DeclStatContext ctx) {

    }

    @Override
    public void enterTrueExpr(DonutParser.TrueExprContext ctx) {

    }

    @Override
    public void exitTrueExpr(DonutParser.TrueExprContext ctx) {

    }

    @Override
    public void enterMultExpr(DonutParser.MultExprContext ctx) {

    }

    @Override
    public void exitMultExpr(DonutParser.MultExprContext ctx) {

    }

    @Override
    public void enterMinusExpr(DonutParser.MinusExprContext ctx) {

    }

    @Override
    public void exitMinusExpr(DonutParser.MinusExprContext ctx) {

    }

    @Override
    public void enterNumExpr(DonutParser.NumExprContext ctx) {

    }

    @Override
    public void exitNumExpr(DonutParser.NumExprContext ctx) {

    }

    @Override
    public void enterPlusExpr(DonutParser.PlusExprContext ctx) {

    }

    @Override
    public void exitPlusExpr(DonutParser.PlusExprContext ctx) {

    }

    @Override
    public void enterParExpr(DonutParser.ParExprContext ctx) {

    }

    @Override
    public void exitParExpr(DonutParser.ParExprContext ctx) {

    }

    @Override
    public void enterCompExpr(DonutParser.CompExprContext ctx) {

    }

    @Override
    public void exitCompExpr(DonutParser.CompExprContext ctx) {

    }

    @Override
    public void enterPrfExpr(DonutParser.PrfExprContext ctx) {

    }

    @Override
    public void exitPrfExpr(DonutParser.PrfExprContext ctx) {

    }

    @Override
    public void enterDivExpr(DonutParser.DivExprContext ctx) {

    }

    @Override
    public void exitDivExpr(DonutParser.DivExprContext ctx) {

    }

    @Override
    public void enterFalseExpr(DonutParser.FalseExprContext ctx) {

    }

    @Override
    public void exitFalseExpr(DonutParser.FalseExprContext ctx) {

    }

    @Override
    public void enterPowExpr(DonutParser.PowExprContext ctx) {

    }

    @Override
    public void exitPowExpr(DonutParser.PowExprContext ctx) {

    }

    @Override
    public void enterBoolExpr(DonutParser.BoolExprContext ctx) {

    }

    @Override
    public void exitBoolExpr(DonutParser.BoolExprContext ctx) {

    }

    @Override
    public void enterIdExpr(DonutParser.IdExprContext ctx) {

    }

    @Override
    public void exitIdExpr(DonutParser.IdExprContext ctx) {

    }

    @Override
    public void enterVarDecl(DonutParser.VarDeclContext ctx) {

    }

    @Override
    public void exitVarDecl(DonutParser.VarDeclContext ctx) {

    }

    @Override
    public void enterIntDecl(DonutParser.IntDeclContext ctx) {

    }

    @Override
    public void exitIntDecl(DonutParser.IntDeclContext ctx) {

    }

    @Override
    public void enterBoolDecl(DonutParser.BoolDeclContext ctx) {

    }

    @Override
    public void exitBoolDecl(DonutParser.BoolDeclContext ctx) {

    }

    @Override
    public void enterCharDecl(DonutParser.CharDeclContext ctx) {

    }

    @Override
    public void exitCharDecl(DonutParser.CharDeclContext ctx) {

    }

    @Override
    public void enterArrayDecl(DonutParser.ArrayDeclContext ctx) {

    }

    @Override
    public void exitArrayDecl(DonutParser.ArrayDeclContext ctx) {

    }

    @Override
    public void enterType(DonutParser.TypeContext ctx) {

    }

    @Override
    public void exitType(DonutParser.TypeContext ctx) {

    }

    @Override
    public void enterBoolOperator(DonutParser.BoolOperatorContext ctx) {

    }

    @Override
    public void exitBoolOperator(DonutParser.BoolOperatorContext ctx) {

    }

    @Override
    public void enterCompOperator(DonutParser.CompOperatorContext ctx) {

    }

    @Override
    public void exitCompOperator(DonutParser.CompOperatorContext ctx) {

    }

    @Override
    public void enterPrfOperator(DonutParser.PrfOperatorContext ctx) {

    }

    @Override
    public void exitPrfOperator(DonutParser.PrfOperatorContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
