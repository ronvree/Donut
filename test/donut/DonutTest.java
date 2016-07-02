package donut;

import donut.checkers.CheckerTest;
import donut.generator.GeneratorTest;
import donut.grammar.GrammarTest;
import org.junit.Assert;
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
        Result res = core.run(CheckerTest.class);
        Assert.assertEquals(0, res.getFailureCount());
        System.out.println("- Checker tests done.");
    }

    @Test
    public void testGenerator() {
        System.out.println("\nGenerator tests:");
        Result res = core.run(GeneratorTest.class);
        Assert.assertEquals(0, res.getFailureCount());
        System.out.println("- Generator tests done.");
    }
}
