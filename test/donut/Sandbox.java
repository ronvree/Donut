package donut;

import com.sun.org.apache.xpath.internal.SourceTree;
import donut.checkers.Checker;
import donut.generators.MainGenerator;
import donut.spril.Program;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import util.HaskelRunner;
import util.HaskellWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the Sandbox.donut file in directory testfiles.
 */
public class Sandbox {

    private static final String BASE_DIR = "src/donut/testfiles/";
    private static final String EXT = ".donut";

    private ArrayList<ArrayList> localMem;
    private ArrayList<Integer> sharedMem;

    public static void main(String[] args) {
        Sandbox s = new Sandbox();
        s.runFile("sandbox");
        System.out.println("Results of sandbox.donut:\n");
        System.out.println("    Local Memory:\n");
        System.out.println("    " + s.localMem);
        System.out.println("\n    Shared Memory: " + s.sharedMem);
    }

    public Sandbox() {
        localMem = new ArrayList<>();
        sharedMem = new ArrayList<>();
    }
    private void runFile(String fileName) {
        DonutParser.ProgramContext programContext = parse(fileName);
        ParseTreeWalker walker = new ParseTreeWalker();
        Checker checker = new Checker();

        walker.walk(checker, programContext);

        MainGenerator generator = new MainGenerator();
        List<Program> programs = generator.generate(programContext, checker.getResult());

        HaskellWriter writer = new HaskellWriter();
        writer.writeFile(programs);

        HaskelRunner runner = new HaskelRunner("result");
        localMem = runner.getLocalMem();
        sharedMem = runner.getSharedMem();
    }

    private static DonutParser.ProgramContext parse(String filename)   {
        CharStream chars = null;
        try {
            chars = new ANTLRInputStream(new FileReader(new File(BASE_DIR + filename + EXT)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Lexer lexer = new DonutLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        DonutParser parser = new DonutParser(tokens);
        return parser.program();
    }
}
