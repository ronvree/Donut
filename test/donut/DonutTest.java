package donut;

import donut.checkers.CheckerIITest;
import donut.grammar.GrammarTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

/**
 * Test the complete Donut language!
 */
public class DonutTest {

    private JUnitCore core = new JUnitCore();

    @Test
    public void testGrammar() {
        Result res = core.run(GrammarTest.class);
        Assert.assertEquals(0, res.getFailureCount());
    }

    @Test
    public void testChecker()   {
        Result res = core.run(CheckerIITest.class);
        Assert.assertEquals(0, res.getFailureCount());
    }

    @Test
    public void testGenerator() {
        // TODO
    }

    @Test
    public void testExecution()  {
        // TODO
    }


}
