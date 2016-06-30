package donut.grammar;

import donut.DonutLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.junit.Test;

public class LexerTest {

    private static final String NUM = "123";

    @Test
    public void testNum()  {
        DonutLexer lexer = new DonutLexer(new ANTLRInputStream(NUM));
        System.out.println(lexer.getAllTokens());
    }

}
