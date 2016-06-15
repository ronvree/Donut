package donut;

import org.antlr.v4.runtime.*;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * Created by Gijs on 15-Jun-16.
 */
public class GrammarTest {

    private static final String FILE = "src/donut/sample/sample1.donut";

    private static final String IntegerDeclaration = "number n = 4;";

    @Test
    public void testArrayDeclaration() {

    }

    @Test
    public void testCharacterDeclaration() {

    }

    @Test
    public void testBooleanDeclaration() {
    }

    @Test
    public void testIntegerDeclaration() {
        List<String> errors = runTest(format(IntegerDeclaration), false);
        Assert.assertTrue(errors.size() == 0);
    }




















    public String format(String testStatement) {
        String beginFile = "Alright brain... it's all up to you {";
        String endFile = "} I hope I didn\'t brain my damage.";
        return beginFile + testStatement + endFile;
    }




    public List<String> runTest(String text, boolean textIsFile) {
        CharStream stream;
        try {
            if (textIsFile) {
                stream = new ANTLRInputStream(new FileInputStream(new File(text)));
            } else {
                stream = new ANTLRInputStream(new StringReader(text));
            }
        } catch (IOException e) {
            return null;
        }
        DonutLexer lexer = new DonutLexer(stream);
        ErrorListener errorListener = new ErrorListener();
        TokenStream tokenStream = new CommonTokenStream(lexer);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        DonutParser parser = new DonutParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        parser.program();
        return errorListener.getErrors();

    }
}
