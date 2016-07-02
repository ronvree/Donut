package donut.checkers;

import donut.DonutBaseListener;
import donut.DonutParser;
import donut.Type;
import donut.errors.*;
import donut.errors.Error;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ron on 23-6-2016.
 *
 * Responsible for:
 *  - Type checking
 *  - Calculating offset
 *  - Correct declarations/visibility of variables in multiple scopes
 *  - Distinguishing between shared and local variables
 */
public class CheckerII extends DonutBaseListener {

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
    private SymbolTableII scopes;

    /**
     * Store resulting type and offset
     */
    private CheckerResultII result;

    public CheckerII()  {
        this.types = new ParseTreeProperty<>();
        this.errors = new ArrayList<>();
        this.scopes = new SymbolTableII();
        this.result = new CheckerResultII();
    }

    public List<Error> getErrors()  {
        return this.errors;
    }
    public CheckerResultII getResult() { return this.result; }

    /*
        Listener methods
     */

    /** Enter block - Open a new scope */
    @Override
    public void enterBlock(DonutParser.BlockContext ctx) {
        this.scopes.openScope();
    }

    /** Exit block - Close the scope */
    @Override
    public void exitBlock(DonutParser.BlockContext ctx) {
        this.scopes.closeScope();
    }

    /** Enter concurrent block - Open a new scope, where only global variables are visible */
    @Override
    public void enterConcurrentBlock(DonutParser.ConcurrentBlockContext ctx) {
        this.scopes.openSharedScope();
    }

    /** Exit concurrent block - Close the scope */
    @Override
    public void exitConcurrentBlock(DonutParser.ConcurrentBlockContext ctx) {
        this.scopes.closeScope();
    }

    /** Enter lock block - Open a new scope */
    @Override
    public void enterLockBlock(DonutParser.LockBlockContext ctx) {
        this.scopes.openScope();
    }

    /** Exit lock block - Close the scope */
    @Override
    public void exitLockBlock(DonutParser.LockBlockContext ctx) {
        this.scopes.closeScope();
    }

    /*
        Statements
     */

    /** Enter lock statement - Checks if the variable to be locked is already declared and if so, also checks
     *  whether the variable is declared globally or not. Only shared variables can be locked. When these
     *  requirements are met, bind the variable data to the ID context node */
    @Override
    public void enterLockStat(DonutParser.LockStatContext ctx) {
        String id = ctx.ID().getText();
        if (scopes.getCurrentScope().contains(id)) {
            if (scopes.getCurrentScope().isShared(id))   {
                this.types.put(ctx.ID(), scopes.getCurrentScope().getType(id));
                this.result.setType(ctx.ID(), this.scopes.getCurrentScope().getType(id));
                this.result.setOffset(ctx.ID(), this.scopes.getCurrentScope().getOffset(id));
                this.result.setShared(ctx.ID(), true);
            } else {
                this.errors.add(new LockError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), id));
            }
        } else {
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), id));
        }
    }

    /**
     * Enter an assign statement
     * Check if the variable ID has already been declared and thus can be referenced to
     *  - if so:  Bind the previously defined type of this variable to the ID context
     *  - else :  Add an error
     */
    @Override
    public void enterAssStat(DonutParser.AssStatContext ctx) {
        if (scopes.contains(ctx.ID().getText()))   {
            this.types.put(ctx.ID(), scopes.getCurrentScope().getType(ctx.ID().getText()));
            this.result.setType(ctx.ID(), this.scopes.getCurrentScope().getType(ctx.ID().getText()));
            this.result.setOffset(ctx.ID(), this.scopes.getCurrentScope().getOffset(ctx.ID().getText()));
            this.result.setShared(ctx.ID(), this.scopes.getCurrentScope().isShared(ctx.ID().getText()));
        } else {
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), ctx.ID().getText()));
        }
    }

    /**
     * Exit an assign statement
     * Check if the ID was bound to a type (by the enter method)
     * Check if the ID type matches the expression type
     *  - if not: Add an error
     */
    @Override
    public void exitAssStat(DonutParser.AssStatContext ctx) {
        Type idType = this.types.get(ctx.ID());
        if (idType != null)   {
            Type exprType = this.types.get(ctx.expr());
            // Check if ID type matches the expression type
            if (!(idType.equals(exprType)))   {
                // ID type does not equal the type of the expression. Add a type error
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), idType, exprType));
            }
        } else {
            // Missing declaration error has already been given in the enter method
        }
    }

    /**
     * Exit an if statement
     * Check if the expression is of type REACTION
     *  - if not: Add an error
     */
    @Override
    public void exitIfStat(DonutParser.IfStatContext ctx) {
        Type type = this.types.get(ctx.expr());
        if (!(type instanceof Type.ReactionType))   {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr().getStart().getCharPositionInLine(), Type.REACTION_TYPE, type));
        }

    }

    /**
     * Exit a while statement
     * Check if the expression is of type REACTION
     *  - if not: Add an error
     */
    @Override
    public void exitWhileStat(DonutParser.WhileStatContext ctx) {
        Type type = this.types.get(ctx.expr());
        if (!(type instanceof Type.ReactionType))   {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr().getStart().getCharPositionInLine(), Type.REACTION_TYPE, type));
        }

    }

    /**
     * Enter a declaration statement
     * Check if the ID has not already been declared in this scope
     *  - if so: Add an error
     *  - else : Add variable to scope, which calculates the offset
     *           Add variable + type + offset to result
     */
    @Override
    public void enterDeclStat(DonutParser.DeclStatContext ctx) {
        Type type = makeType(ctx.type());
        this.types.put(ctx.ID(), type);
        if (this.scopes.contains(ctx.ID().getText()))   {
            this.errors.add(new DoubleDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), ctx.ID().getText()));
        } else {
            boolean shared = ctx.GLOBAL() != null;
            this.scopes.put(ctx.ID().getText(), type, shared);
            this.result.setType(ctx.ID(), type);
            this.result.setOffset(ctx.ID(), this.scopes.getCurrentScope().getOffset(ctx.ID().getText()));
            this.result.setShared(ctx.ID(), shared);
        }
    }

    /**
     * Exit a declaration statement
     * Make sure the statement does not declare itself (e.g. number x = x;)
     * Add an error if this is the case
     * Otherwise ensure that the ID type matches the expression type
     * Add an error if this is not the case
     */
    @Override
    public void exitDeclStat(DonutParser.DeclStatContext ctx) {
        Type idType = this.types.get(ctx.ID());
        Type exprType = this.types.get(ctx.expr());
        if (ctx.expr() instanceof DonutParser.IdExprContext && ctx.expr().getText().equals(ctx.ID().getText()))   {
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), ctx.ID().getText()));
        } else {
            if (!(idType.equals(exprType)) && ctx.ASSIGN() != null)   {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), idType, exprType));
            }
        }
    }


    /**
     * Help method to determine the type of a TypeContext
     * This is necessary because comparing TypeContext is not sufficient (TypeContext can be of the form: ARRAYTYPE TypeContext)
     * This method can construct nested Array types (e.g. bunchof bunchof number)
     */
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

    /**
     * Enter an array expression
     * This expression constructs a new array
     * Construct the type of the array
     * Bind this type to the expression
     */
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
            type = null;
        }
        for (int i = 0; i < ctx.getChildCount() - 2; i += 2)  {
            type = new Type.ArrayType(type);
        }
        this.types.put(ctx, type);
    }

    /*
        -- Number operators
     */

    /*
        -- -- Multiplication/Division
     */

    /**
     * Exit multiplication/division expression
     * Check if both expressions are of type NUMBER
     *  - if so: Set the type of this expression to NUMBER
     *  - else : Add an error
     */
    @Override
    public void exitMultExpr(DonutParser.MultExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        this.types.put(ctx, Type.NUMBER_TYPE);
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- multiplying/dividing numbers
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }


    }

    /*
        -- -- Addition/Subtraction
     */

    /**
     * Exit addition/subtraction expression
     * Check if both expressions are of type NUMBER
     *  - if so: Set the type of this expression to NUMBER
     *  - else : Add an error
     */
    @Override
    public void exitPlusExpr(DonutParser.PlusExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        this.types.put(ctx, Type.NUMBER_TYPE);
        // Check types
        if (t1 instanceof Type.NumberType) {
            // OK
            if (t2 instanceof Type.NumberType) {
                // OK -- addition/subtraction with numbers
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

    }

    /*
        -- -- Powers
     */

    /**
     * Exit power expression
     * Check if both expressions are of type NUMBER
     *  - if so: Set the type of this expression to NUMBER
     *  - else : Add an error
     */
    @Override
    public void exitPowExpr(DonutParser.PowExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        this.types.put(ctx, Type.NUMBER_TYPE);
        // Check types
        if (t1 instanceof Type.NumberType)   {
            // OK
            if (t2 instanceof Type.NumberType)   {
                // OK -- powers of numbers
            } else {
                // NOT OK -- second argument not a number!
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t2));
            }
        } else {
            // NOT OK -- first argument not a number!
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(0).getStart().getCharPositionInLine(), Type.NUMBER_TYPE, t1));
        }

    }

    /*
        -- Comparison operators
     */

    /**
     * Exit comparison expression
     * Check if both expressions are of type NUMBER
     *  - if so: Set the type of this expression to REACTION
     *  - else : Add an error
     */
    @Override
    public void exitCompExpr(DonutParser.CompExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        this.types.put(ctx, Type.REACTION_TYPE);
        // Compare types
        if (t1 instanceof Type.NumberType)   {
            if (t2 instanceof Type.NumberType)   {
                // OK - comparing numbers
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

    /**
     * Exit prefix expression
     * Check if the expression is of type NUMBER (-) or REACTION (not)
     *  - if so: Set the type of this expression to NUMBER or REACTION respectively
     *  - else : Add an error
     */
    @Override
    public void exitPrfExpr(DonutParser.PrfExprContext ctx) {
        Type type = this.types.get(ctx.expr());
        if (ctx.prfOperator().MINUS() != null)   {
            this.types.put(ctx, Type.NUMBER_TYPE);
            if (!(type instanceof Type.NumberType))   {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.prfOperator().getStart().getCharPositionInLine(), Type.NUMBER_TYPE, type));
            }
        } else if (ctx.prfOperator().NOT() != null) {
            this.types.put(ctx, Type.REACTION_TYPE);
            if (!(type instanceof Type.ReactionType))   {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.prfOperator().getStart().getCharPositionInLine(), Type.REACTION_TYPE, type));
            }
        } else {
            System.out.println("Can't recognize this prefix!");
        }

    }

    /**
     * Exit boolean expression
     * Check if both expressions are of type REACTION
     *  - if so: Set the type of this expression to REACTION
     *  - else : Add an error
     */
    @Override
    public void exitBoolExpr(DonutParser.BoolExprContext ctx) {
        Type t1 = this.types.get(ctx.expr(0));
        Type t2 = this.types.get(ctx.expr(1));
        this.types.put(ctx, Type.REACTION_TYPE);
        // Check types
        if (t1 instanceof Type.ReactionType)   {
            if (t2 instanceof Type.ReactionType)   {
                // OK - evaluating reactions
            } else {
                this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.REACTION_TYPE, t2));
            }
        } else {
            this.errors.add(new TypeError(ctx.start.getLine(), ctx.expr(1).getStart().getCharPositionInLine(), Type.REACTION_TYPE, t1));
        }

    }

    /**
     * Enter ID expression
     * Check if the ID has been declared and thus can be referenced to
     *  - if so: Query the offset and type from the scope
     *           Bind the type to the ID context
     *           Bind the type and offset to the ID Context in result
     *  - else : Add an error
     */
    @Override
    public void enterIdExpr(DonutParser.IdExprContext ctx) {
        String id = ctx.ID().getText();
        if (this.scopes.contains(id)) {
            int offset = scopes.getCurrentScope().getOffset(id);
            Type type = scopes.getCurrentScope().getType(id);
            boolean shared = scopes.getCurrentScope().isShared(id);
            this.types.put(ctx, type);
            this.result.setOffset(ctx.ID(), offset);
            this.result.setType(ctx.ID(), type);
            this.result.setShared(ctx.ID(), shared);
        } else {
            // ID not declared!
            this.errors.add(new MissingDeclError(ctx.start.getLine(), ctx.ID().getSymbol().getCharPositionInLine(), id));
        }

    }

    /*
        -- Parenthesis
     */

    /**
     * Exit parenthesis expression
     * Makes sure the type bounded to the child expression gets bounded to this expression
     */
    @Override
    public void exitParExpr(DonutParser.ParExprContext ctx) {
        this.types.put(ctx, this.types.get(ctx.expr()));
    }
}
