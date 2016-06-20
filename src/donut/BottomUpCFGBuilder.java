package donut;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import donut.errors.Error;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Gijs on 20-Jun-16.
 */
public class BottomUpCFGBuilder {

    /** The CFG being built. */
    private Graph graph;

    private String fileName;

    /** Builds the CFG for a program given by a filename. */
    public Graph build(String fileName) {
        this.fileName = fileName;
        return build(new File(fileName));
    }

    /** Builds the CFG for a program contained in a given file. */
    public Graph build(File file) {
        Graph result = null;
        ErrorListener listener = new ErrorListener();
        try {
            CharStream chars = new ANTLRInputStream(new FileReader(file));
            DonutLexer lexer = new DonutLexer(chars);
            lexer.removeErrorListeners();
            lexer.addErrorListener(listener);
            TokenStream tokens = new CommonTokenStream(lexer);
            DonutParser parser = new DonutParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(listener);
            ParseTree tree = parser.program();
            if (listener.hasErrors()) {
                System.out.printf("Parse errors in %s:%n", file.getPath());
                for (Error error : listener.getErrors()) {
                    System.err.println(error.toString());
                }
            } else {
                result = build(tree);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /** Builds the CFG for a program given as an ANTLR parse tree. */
    public Graph build(ParseTree tree) {
        this.graph = new Graph();
        ParseTreeWalker walker = new ParseTreeWalker();
        BottomUpListener listener = new BottomUpListener(graph);
        walker.walk(listener, tree);
        this.graph = listener.getGraph();
        return graph;
    }

    public void printGraph(String filename) {
        try {
            graph.writeDOT(filename.substring(0, filename.length()-5) + "Result.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /** Adds a node to he CGF, based on a given parse tree node.
     * Gives the CFG node a meaningful ID, consisting of line number and
     * a further indicator.
     */
    private Node addNode(ParserRuleContext node, String text) {
        return this.graph.addNode(node.getStart().getLine() + ": " + text);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: [filename]+");
            return;
        }
        BottomUpCFGBuilder builder = new BottomUpCFGBuilder();
        for (String filename : args) {
            File file = new File(filename);
            System.out.println(filename);
            System.out.println(builder.build(file));
            builder.printGraph(filename);
        }
    }

}

