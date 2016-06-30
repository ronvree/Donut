package donut.generator;

import donut.*;
import donut.checkers.CheckerII;
import donut.generators.MainGenerator;
import donut.spril.Program;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;
import util.HaskelRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gijs on 30-6-2016.
 */
public class GeneratorTest {

    private static final String BASE_DIR = "src/donut/sample/";
    private static final String EXT = ".donut";

    private ArrayList<ArrayList> localMem;
    private ArrayList<Integer> sharedMem;


    @Test
    public void runTest() {
        String fileName = "threads2";
        DonutParser.ProgramContext programContext = parse(fileName);
        ParseTreeWalker walker = new ParseTreeWalker();
        CheckerII checker = new CheckerII();

        walker.walk(checker, programContext);

        MainGenerator generator = new MainGenerator();
        List<Program> programs = generator.generate(programContext, checker.getResult());

        HaskellWriter writer = new HaskellWriter();
        writer.writeFile(programs);

        HaskelRunner runner = new HaskelRunner();
        runner.runHaskell("threadResult.hs");

        this.localMem = runner.getLocalMem();
        this.sharedMem = runner.getSharedMem();
    }

    private DonutParser.ProgramContext parse(String filename)   {
        CharStream chars = null;
        try {
            chars = new ANTLRInputStream(new FileReader(new File(BASE_DIR + filename + EXT)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Lexer lexer = new DonutLexer(chars);
        TokenStream tokens = new CommonTokenStream(lexer);
        DonutParser parser = new DonutParser(tokens);
        DonutParser.ProgramContext prog = parser.program();
        return prog;
    }

    public ArrayList<ArrayList> getLocalMem() {
        return localMem;
    }

    public ArrayList<Integer> getSharedMem() {
        return sharedMem;
    }
}
