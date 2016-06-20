package donut;

import donut.errors.DoubleDeclError;
import donut.errors.Error;
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
    private List<Error> errors;
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

    public List<Error> getErrors()  {
        return this.errors;
    }

    /*
        Listener methods
     */

    @Override
    public void enterProgram(DonutParser.ProgramContext ctx) {

    }

    @Override
    public void exitProgram(DonutParser.ProgramContext ctx) {

    }


    @Override
    public void enterBlock(DonutParser.BlockContext ctx) {
        this.scopes.openScope();
    }

    @Override
    public void exitBlock(DonutParser.BlockContext ctx) {
        this.scopes.closeScope();
    }

    /*
        Statements
     */

    @Override
    public void enterAssStat(DonutParser.AssStatContext ctx) {
        if (scopes.contains(ctx.ID().getText()))   {
            this.types.put(ctx.ID(), scopes.getCurrentScope().getType(ctx.ID().getText()));
        } else {
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), ctx.ID().getText()));
        }
    }

    @Override
    public void exitAssStat(DonutParser.AssStatContext ctx) {
        Type idType = this.types.get(ctx.ID());
        if (idType != null)   {
            Type exprType = this.types.get(ctx.expr());
            if (!(idType.equals(exprType)))   {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), idType, exprType));
            }
        } else {
            // Missing declaration error has already been given in the enter method
        }
    }

    @Override
    public void enterIfStat(DonutParser.IfStatContext ctx) {

    }

    @Override
    public void exitIfStat(DonutParser.IfStatContext ctx) {
        Type type = this.types.get(ctx.expr());
        if (!(type instanceof Type.ReactionType))   {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr().getStart().getCharPositionInLine(), Type.REACTION_TYPE, type));
        }
    }

    @Override
    public void enterWhileStat(DonutParser.WhileStatContext ctx) {

    }

    @Override
    public void exitWhileStat(DonutParser.WhileStatContext ctx) {
        Type type = this.types.get(ctx.expr());
        if (!(type instanceof Type.ReactionType))   {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr().getStart().getCharPositionInLine(), Type.REACTION_TYPE, type));
        }
    }

    @Override
    public void enterDeclStat(DonutParser.DeclStatContext ctx) {
        Type type = makeType(ctx.type());
        this.types.put(ctx.ID(), type);
        if (this.scopes.contains(ctx.ID().getText()))   {
            this.errors.add(new DoubleDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), ctx.ID().getText()));
        } else {
            this.scopes.put(ctx.ID().getText(), type);
            this.result.setType(ctx.ID(), type);
            this.result.setOffset(ctx.ID(), this.scopes.getCurrentScope().getOffset(ctx.ID().getText()));
        }

        this.result.setEntry(ctx, ctx); // TODO -- set entries correctly
    }

    @Override
    public void exitDeclStat(DonutParser.DeclStatContext ctx) {
        Type idType = this.types.get(ctx.ID());
        Type exprType = this.types.get(ctx.expr());

        if (ctx.expr() instanceof DonutParser.IdExprContext && ctx.expr().getText().equals(ctx.ID().getText()))   {
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), ctx.ID().getText()));
        } else {

        }

        if (!(idType.equals(exprType)) && ctx.ASSIGN() != null)   {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), idType, exprType));
        }

    }

    private Type makeType(DonutParser.TypeContext ctx) {
        Type type;
        if (ctx.ARRAYTYPE() != null)   {
            type = makeType(ctx.type());
            type = new Type.ArrayType(type);
        } else if (ctx.INTTYPE() != null)    {
            type = Type.NUMBER_TYPE;
        } else if (ctx.BOOLEANTYPE() != null)    {
            type = Type.REACTION_TYPE;
        } else if (ctx.CHARTYPE() != null)    {
            type = Type.SYMBOL_TYPE;
        } else {
            System.out.println("Could not recognize type in Checker.makeType");
            type = null;
        }
        return type;
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
        DonutParser.TypeContext typeContext = ctx.type();
        Type type;
        if (typeContext.BOOLEANTYPE() != null)   {
            type = Type.REACTION_TYPE;
        } else if (typeContext.CHARTYPE() != null)    {
            type = Type.SYMBOL_TYPE;
        } else if (typeContext.INTTYPE() != null)    {
            type = Type.NUMBER_TYPE;
        } else {
            System.out.println("Unsupported type in Checker.makeArrayType");
            type = null;
        }
        for (int i = 0; i < ctx.getChildCount(); i += 2)  {
            type = new Type.ArrayType(type);
        }
        this.types.put(ctx, type);
    }

    /*
        -- Number operators
     */

    /*
        -- -- Multiplication
     */

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
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

        // Set CFG entry
        this.result.setEntry(ctx, ctx.expr(0));

    }

    /*
        -- -- Division
     */

    @Override
    public void enterDivExpr(DonutParser.DivExprContext ctx) {

    }

    @Override
    public void exitDivExpr(DonutParser.DivExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- division with numbers
                this.types.put(ctx, Type.NUMBER_TYPE);
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

        // Set CFG entry
        this.result.setEntry(ctx, ctx.expr(0));
    }

    /*
        -- -- Addition
     */

    @Override
    public void enterPlusExpr(DonutParser.PlusExprContext ctx) {

    }

    @Override
    public void exitPlusExpr(DonutParser.PlusExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- addition with numbers
                this.types.put(ctx, Type.NUMBER_TYPE);
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

        // Set CFG entry
        this.result.setEntry(ctx, ctx.expr(0));
    }

    /*
        -- -- Subtraction
     */

    @Override
    public void enterMinusExpr(DonutParser.MinusExprContext ctx) {

    }

    @Override
    public void exitMinusExpr(DonutParser.MinusExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- subtraction with numbers
                this.types.put(ctx, Type.NUMBER_TYPE);
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

        // Set CFG entry
        this.result.setEntry(ctx, ctx.expr(0));
    }

    /*
        -- -- Powers
     */

    @Override
    public void enterPowExpr(DonutParser.PowExprContext ctx) {

    }

    @Override
    public void exitPowExpr(DonutParser.PowExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- powers of numbers
                this.types.put(ctx, Type.NUMBER_TYPE);
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

        // Set CFG entry
        this.result.setEntry(ctx, ctx.expr(0));
    }

    /*
        -- Comparison operators
     */

    @Override
    public void enterCompExpr(DonutParser.CompExprContext ctx) {

    }

    @Override
    public void exitCompExpr(DonutParser.CompExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Compare types
        if (t1 instanceof Type.NumberType)   {
            if (t2 instanceof Type.NumberType)   {
                // OK - comparing numbers
                this.types.put(ctx, Type.REACTION_TYPE);
            } else {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }
    }

    /*
        -- Prefix
     */

    @Override
    public void enterPrfExpr(DonutParser.PrfExprContext ctx) {

    }

    @Override
    public void exitPrfExpr(DonutParser.PrfExprContext ctx) {
        Type type = this.types.get(ctx.expr());
        if (ctx.prfOperator().MINUS() != null)   {
            if (type instanceof Type.NumberType)   {
                this.types.put(ctx, Type.NUMBER_TYPE);
            } else {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.prfOperator().getStart().getCharPositionInLine(), Type.NUMBER_TYPE, type));
            }
        } else if (ctx.prfOperator().NOT() != null) {
            if (type instanceof Type.ReactionType)   {
                this.types.put(ctx, Type.REACTION_TYPE);
            } else {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.prfOperator().getStart().getCharPositionInLine(), Type.REACTION_TYPE, type));
            }
        } else {
            System.out.println("Can't recognize this prefix!");
        }
    }

    @Override
    public void enterBoolExpr(DonutParser.BoolExprContext ctx) {

    }

    @Override
    public void exitBoolExpr(DonutParser.BoolExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        // Check types
        if (t1 instanceof Type.ReactionType)   {
            if (t2 instanceof Type.ReactionType)   {
                // OK - evaluating reactions
                this.types.put(ctx, Type.REACTION_TYPE);
            } else {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.REACTION_TYPE, t2));
            }
        } else {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.REACTION_TYPE, t1));
        }
    }

    @Override
    public void enterIdExpr(DonutParser.IdExprContext ctx) {
        String id = ctx.ID().getText();
        if (this.scopes.contains(id)) {
            int offset = scopes.getCurrentScope().getOffset(id);
            Type type = scopes.getCurrentScope().getType(id);
            this.types.put(ctx, type);
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
        -- Parenthesis
     */

    @Override
    public void enterParExpr(DonutParser.ParExprContext ctx) {

    }

    @Override
    public void exitParExpr(DonutParser.ParExprContext ctx) {
        this.types.put(ctx, this.types.get(ctx.expr()));
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
