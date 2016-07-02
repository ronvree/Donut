package donut;

import donut.checkers.CheckerIITest;
import donut.execution.PetersonTest;
import donut.generator.GeneratorTest;
import donut.generators.CodeGenerator;
import donut.grammar.GrammarTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Test the execution Donut language automatically!
 */
public class DonutTest {

    private JUnitCore core = new JUnitCore();

    @Test
    public void testGrammar() {
        Result res = core.run(GrammarTest.class);
        Assert.assertEquals(0, res.getFailureCount());
        System.out.println("- Grammar tests done.");
    }

    @Test
    public void testChecker()   {
        Result res = core.run(CheckerIITest.class);
        Assert.assertEquals(0, res.getFailureCount());
        System.out.println("- Checker tests done.");
    }

    @Test
    public void testGenerator() {
        System.out.println("\nGenerator tests:");
        Result res = core.run(GeneratorTest.class);
        Assert.assertEquals(0, res.getFailureCount());


        // Peterson test.
        int previousThreads = CodeGeneratorII.THREADS;
        setNumberOfThreads(2);                              // PetersonTest must be executed on 2 threads.
        Result pres = core.run(PetersonTest.class);
        Assert.assertEquals(0, pres.getFailureCount());
        System.out.println("    - Peterson test done.");
        setNumberOfThreads(previousThreads);

        System.out.println("- Generator tests done.");

    }

    private void setNumberOfThreads(int numberOfThreads) {
        CodeGeneratorII.THREADS = numberOfThreads;

    }
}
