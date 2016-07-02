package donut;

import donut.DonutLexer;
import donut.DonutParser;
import donut.generators.CodeGenerator;
import util.HaskellWriter;
import donut.generators.MainGenerator;
import donut.checkers.Checker;
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

    private static final String BASE_DIR = "src/donut/testfiles/generator/";
    private static final String EXT = ".donut";

    private ArrayList<ArrayList> localMem;
    private ArrayList<Integer> sharedMem;

    @Test
    public void petersonTest()  {
        int previous = MainGenerator.THREADS;
        MainGenerator.THREADS = 2;
        if (MainGenerator.THREADS == 2) {
            this.runTest("peterson");
            Assert.assertEquals(1000, sharedMem.get(sharedVarIndex()).intValue());
        } else {
            System.err.println("IMPORTANT: For petersonTest to work, only two threads need to be used (change CodeGenerator's THREAD constant)");
        }
        MainGenerator.THREADS = previous;
        System.out.println("    - Peterson test done.");
    }

    /** Calculates the starting index of variables in shared memory */
    private static int sharedVarIndex() {
        return THREADS + (SHAREDMEMSIZE - THREADS) / 2;
    }

    private void runTest(String fileName) {
        DonutParser.ProgramContext programContext = parse(fileName);
        ParseTreeWalker walker = new ParseTreeWalker();
        Checker checker = new Checker();

        walker.walk(checker, programContext);

        MainGenerator generator = new MainGenerator();
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
