package donut.generator;

import donut.*;
import donut.checkers.Checker;
import donut.generators.MainGenerator;
import donut.spril.Program;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;
import util.HaskelRunner;
import util.HaskellWriter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorTest {

    private static final String BASE_DIR = "src/donut/testfiles/generator/";
    private static final String EXT = ".donut";

    private ArrayList<ArrayList> localMem;
    private ArrayList<Integer> sharedMem;

    @Test
    public void bankTest() {
        this.runTest("bank");
        Assert.assertEquals(1000, (int) sharedMem.get(9));      // Homers' balance.
        Assert.assertEquals(200, (int) sharedMem.get(10));      // Marge's balance.
        Assert.assertEquals(1700, (int) sharedMem.get(11));     // Balance.
        System.out.println("    - Bank test done.");
    }

    @Test
    public void gcdTest() {
        this.runTest("gcd");
        Assert.assertEquals(3, (int) localMem.get(0).get(1));   // GCD of 9 and 6.
        System.out.println("    - GCD test done.");
    }

    @Test
    public void producerConsumerTest() {
        this.runTest("threads");
        Assert.assertEquals(10, (int) sharedMem.get(9));        // Value of a.
        Assert.assertEquals(0, (int) sharedMem.get(10));        // Value of b.
        System.out.println("    - Producer-consumer test done.");
    }

    /*
        Help methods
     */

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
