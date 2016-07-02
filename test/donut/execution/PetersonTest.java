package donut.execution;

import donut.DonutLexer;
import donut.DonutParser;
import donut.HaskellWriter;
import donut.MainGeneratorII;
import donut.checkers.CheckerII;
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

import static donut.generators.CodeGenerator.SHAREDMEMSIZE;
import static donut.generators.CodeGenerator.THREADS;

public class PetersonTest {

    private static final String BASE_DIR = "src/donut/sample/";
    private static final String EXT = ".donut";

    private ArrayList<ArrayList> localMem;
    private ArrayList<Integer> sharedMem;

    @Test
    public void petersonTest()  {
        Assert.assertEquals(2, MainGeneratorII.THREADS); // IMPORTANT: For this test to work, only two threads need to be used (change CodeGenerator's THREAD constant)
        this.runTest("peterson");
        Assert.assertEquals(2000, sharedMem.get(sharedVarIndex()).intValue());
        System.out.println("- Peterson test done.");
    }

    /** Calculates the starting index of variables in shared memory */
    private static int sharedVarIndex() {
        return THREADS + (SHAREDMEMSIZE - THREADS) / 2;
    }

    private void runTest(String fileName) {
        DonutParser.ProgramContext programContext = parse(fileName);
        ParseTreeWalker walker = new ParseTreeWalker();
        CheckerII checker = new CheckerII();

        walker.walk(checker, programContext);

        MainGeneratorII generator = new MainGeneratorII();
        List<Program> programs = generator.generate(programContext, checker.getResult());

        HaskellWriter writer = new HaskellWriter();
        writer.writeFile(programs);

        HaskelRunner runner = new HaskelRunner("result");
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
        return parser.program();
    }

}
