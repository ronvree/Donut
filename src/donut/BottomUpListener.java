package donut;

import donut.graphAssets.Graph;
import donut.graphAssets.Node;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by Gijs on 20-Jun-16.
 */
public class BottomUpListener extends DonutBaseListener {

    private Graph graph;
    private ParseTreeProperty<Node> in;
    private ParseTreeProperty<Node> out;

    public Graph getGraph() {
        return this.graph;
    }

    public BottomUpListener(Graph graph) {
        this.graph = graph;
        in = new ParseTreeProperty<>();
        out = new ParseTreeProperty<>();
    }

    @Override
    public void exitProgram(DonutParser.ProgramContext ctx) {
        Node node = graph.addNode("Start");
        in.put(ctx, node);
        out.put(ctx, node);

        if (ctx.getChildCount() > 0) {
            node.addEdge(in.get(ctx.getChild(0)));
        }

        for (int i = 0; (i+1) < ctx.getChildCount(); i++) {
            if (out.get(ctx.getChild(i)) != null && in.get(ctx.getChild(i+1)) != null) {
                Node outNode = out.get((ctx.getChild(i)));
                Node inNode = in.get(ctx.getChild(i+1));
                outNode.addEdge(inNode);
            }
        }

        Node endNode = graph.addNode("EndNode");


        // Add last node of statements in block to endnode
        out.get(ctx.block().stat().get(ctx.block().stat().size() - 1)).addEdge(endNode);
    }

    @Override
    public void exitBlock(DonutParser.BlockContext ctx) {
        Node node;
        if (ctx.stat().size() > 0) {
            // In from block is first statement of all statements in this block.
            node = in.get(ctx.stat().get(0));
            in.put(ctx, in.get(ctx.stat().get(0)));

            // Connect each statement to the next one. Except for the last one.
            for (int i = 0; (i+1) < ctx.stat().size(); i++) {
                Node outNode = out.get(ctx.stat(i));
                Node inNode = in.get(ctx.stat(i+1));

                outNode.addEdge(inNode);
            }

            // Out from block is last statement of all statements in this block.
            out.put(ctx, out.get(ctx.stat().get(ctx.stat().size() - 1)));
        } else {
            // No statements in this block
            node = graph.addNode("No statement in block");
            in.put(ctx, node);                 // TODO - or just put null...?
            out.put(ctx, node);
        }

    }

    @Override
    public void exitAssStat(DonutParser.AssStatContext ctx) {
        Node node = graph.addNode(ctx.ID().getText() + " " + ctx.ASSIGN().getText() + " " + ctx.expr().getText());
        in.put(ctx, node);
        out.put(ctx, node);

        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (in.get(ctx.getChild(i)) != null) {
                node.addEdge(in.get(ctx.getChild(i)));
            }
        }
    }

    @Override
    public void exitIfStat(DonutParser.IfStatContext ctx) {
        Node iff = graph.addNode(ctx.expr().getText());
        Node after = graph.addNode("After");

        in.put(ctx, iff);
        out.put(ctx, after);

        if (ctx.getChildCount() > 5) {
            // If statement has an else statement as well.
            iff.addEdge(in.get(ctx.block().get(0)));
            iff.addEdge(in.get(ctx.block().get(1)));

            out.get(ctx.block().get(0)).addEdge(after);
            out.get(ctx.block().get(1)).addEdge(after);


        } else {
            // No else statement.
            iff.addEdge(in.get(ctx.block().get(0)));
            out.get(ctx.block().get(0)).addEdge(after);
        }
    }

    @Override
    public void exitWhileStat(DonutParser.WhileStatContext ctx) {
        super.exitWhileStat(ctx);

    }

    @Override
    public void exitDeclStat(DonutParser.DeclStatContext ctx) {
        Node node;
        if (ctx.getChildCount() > 2) {
            node = graph.addNode(ctx.type().getText() + " "  + ctx.ID() + " " + ctx.expr().getText());
        } else {
            // No assignment.
            node = graph.addNode(ctx.type().getText() + " "  + ctx.ID());
        }

        out.put(ctx, node);
        in.put(ctx, node);

    }




    @Override
    public void exitCharExpr(DonutParser.CharExprContext ctx) {
        super.exitCharExpr(ctx);
    }

    @Override
    public void exitArrayExpr(DonutParser.ArrayExprContext ctx) {
        super.exitArrayExpr(ctx);
    }

    @Override
    public void exitTrueExpr(DonutParser.TrueExprContext ctx) {
        super.exitTrueExpr(ctx);
    }

    @Override
    public void exitMultExpr(DonutParser.MultExprContext ctx) {
        super.exitMultExpr(ctx);
    }

    @Override
    public void exitMinusExpr(DonutParser.MinusExprContext ctx) {
        super.exitMinusExpr(ctx);
    }

    @Override
    public void exitNumExpr(DonutParser.NumExprContext ctx) {
        super.exitNumExpr(ctx);
    }

    @Override
    public void exitPlusExpr(DonutParser.PlusExprContext ctx) {
        super.exitPlusExpr(ctx);
    }

    @Override
    public void exitParExpr(DonutParser.ParExprContext ctx) {
        super.exitParExpr(ctx);
    }

    @Override
    public void exitCompExpr(DonutParser.CompExprContext ctx) {
        super.exitCompExpr(ctx);
    }

    @Override
    public void exitPrfExpr(DonutParser.PrfExprContext ctx) {
        super.exitPrfExpr(ctx);
    }

    @Override
    public void exitDivExpr(DonutParser.DivExprContext ctx) {
        super.exitDivExpr(ctx);
    }

    @Override
    public void exitFalseExpr(DonutParser.FalseExprContext ctx) {
        super.exitFalseExpr(ctx);
    }

    @Override
    public void exitPowExpr(DonutParser.PowExprContext ctx) {
        super.exitPowExpr(ctx);
    }

    @Override
    public void exitBoolExpr(DonutParser.BoolExprContext ctx) {
        super.exitBoolExpr(ctx);
    }

    @Override
    public void exitIdExpr(DonutParser.IdExprContext ctx) {
        super.exitIdExpr(ctx);
    }

    @Override
    public void exitType(DonutParser.TypeContext ctx) {
        super.exitType(ctx);
    }

    @Override
    public void exitBoolOperator(DonutParser.BoolOperatorContext ctx) {
        super.exitBoolOperator(ctx);
    }

    @Override
    public void exitCompOperator(DonutParser.CompOperatorContext ctx) {
        super.exitCompOperator(ctx);
    }

    @Override
    public void exitPrfOperator(DonutParser.PrfOperatorContext ctx) {
        super.exitPrfOperator(ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);
    }
}
