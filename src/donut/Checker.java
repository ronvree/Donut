package donut;

import donut.errors.MissingDeclError;
import donut.errors.TypeError;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ron on 19-6-2016.
 */
public class Checker implements DonutListener {

    /**
     * Temporarily store node types needed for type checking
     */
    private ParseTreeProperty<Type> types;
    /**
     * Store errors that were encountered
     */
    private List<donut.errors.Error> errors;
    /**
     * Keep track of scopes
     */
    private SymbolTable scopes;
    /**
     * Store resulting type and offset
     */
    private CheckerResult result;

    public Checker()  {
        this.types = new ParseTreeProperty<>();
        this.errors = new ArrayList<>();
        this.scopes = new SymbolTable();
        this.result = new CheckerResult();
    }

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

    /*
        Statements
     */

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
        this.scopes.put(ctx.ID().getText(), Type.NUMBER_TYPE);
        this.result.setType(ctx.ID(), Type.NUMBER_TYPE);
        this.result.setOffset(ctx.ID(), this.scopes.getCurrentScope().getOffset(ctx.ID().getText()));

        this.result.setEntry(ctx, ctx); // TODO -- set entries correctly
    }

    @Override
    public void exitDeclStat(DonutParser.DeclStatContext ctx) {
        // TODO -- check if expr type is equal to decl type
    }

    /*
        Expressions
     */

    /** The NUM expr is of type number */
    @Override
    public void enterNumExpr(DonutParser.NumExprContext ctx) {
        this.types.put(ctx, Type.NUMBER_TYPE);
    }

    /** True expr is of type REACTION */
    @Override
    public void enterTrueExpr(DonutParser.TrueExprContext ctx) {
        this.types.put(ctx, Type.REACTION_TYPE);
    }

    /** False expr is of type REACTION */
    @Override
    public void enterFalseExpr(DonutParser.FalseExprContext ctx) {
        this.types.put(ctx, Type.REACTION_TYPE);
    }

    /** Char expr is of type SYMBOL */
    @Override
    public void enterCharExpr(DonutParser.CharExprContext ctx) {
        this.types.put(ctx, Type.SYMBOL_TYPE);
    }

    @Override
    public void enterArrayExpr(DonutParser.ArrayExprContext ctx) {
        // TODO -- make new array type
    }

    @Override
    public void enterMultExpr(DonutParser.MultExprContext ctx) {

    }

    @Override
    public void exitMultExpr(DonutParser.MultExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- multiplying numbers
                this.types.put(ctx, Type.NUMBER_TYPE);
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(-1, -1, Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(-1, -1, Type.NUMBER_TYPE, t1));
        }

        // Set CFG entry
        this.result.setEntry(ctx, ctx.expr(0));

    }

    @Override
    public void enterMinusExpr(DonutParser.MinusExprContext ctx) {

    }

    @Override
    public void exitMinusExpr(DonutParser.MinusExprContext ctx) {

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
        String id = ctx.ID().getText();
        if (this.scopes.contains(id)) {
            int offset = scopes.getCurrentScope().getOffset(id);
            Type type = scopes.getCurrentScope().getType(id);
            this.result.setOffset(ctx.ID(), offset);
            this.result.setType(ctx.ID(), type);
        } else {
            // ID not declared!
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), id));
        }

    }

    @Override
    public void exitIdExpr(DonutParser.IdExprContext ctx) {

    }

    /*
        Other
     */

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


    /*
     *  Unused methods
     */

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

    @Override
    public void exitNumExpr(DonutParser.NumExprContext ctx) {

    }

    @Override
    public void exitTrueExpr(DonutParser.TrueExprContext ctx) {

    }

    @Override
    public void exitFalseExpr(DonutParser.FalseExprContext ctx) {

    }

    @Override
    public void exitCharExpr(DonutParser.CharExprContext ctx) {

    }

    @Override
    public void exitArrayExpr(DonutParser.ArrayExprContext ctx) {

    }

}
