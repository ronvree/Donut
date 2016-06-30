package donut;

import donut.DonutLexer;
import donut.DonutParser;
import donut.ErrorListener;
import donut.errors.Error;
import org.antlr.v4.runtime.*;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.List;

/**
 * Created by Gijs on 15-Jun-16.
 */
public class GrammarTest {


    // Test files with a full program that have to pass the test.
    private static final String EmtpyFile = "src/donut/sample/testfiles/grammartests/testEmpty.donut";
    private static final String IfStatementsFile = "src/donut/sample/testfiles/grammartests/testIfStatements.donut";
    private static final String MargeFile = "src/donut/sample/testfiles/grammartests/testMarge.donut";
    private static final String NumberOperatorsFile = "src/donut/sample/testfiles/grammartests/testNumberOperators.donut";
    private static final String NumbersFile = "src/donut/sample/testfiles/grammartests/testNumbers.donut";
    private static final String ReactionFile = "src/donut/sample/testfiles/grammartests/testReaction.donut";
    private static final String ReactionOperatorsFile = "src/donut/sample/testfiles/grammartests/testReactionOperators.donut";



    //Tests for certain parts of the code that have to pass the test.

    private static final String IntegerDeclaration = "number n = 4;";
    private static final String IntegerDeclaration2 = "number j;";
    private static final String ArrayDeclaration = "bunchof number a = bunchof 6 number;";
    private static final String ArrayOfArraysDeclaration = "bunchof bunchof bunchof number a = bunchof 5 bunchof 4 bunchof 3 number;";
    private static final String CharacterDeclaration = "symbol c = \'%\';";
    private static final String CharacterDeclaration2 = "symbol c;";
    private static final String BooleanDeclaration = "reaction r = WOOHOO;";
    private static final String BooleanDeclaration2 = "reaction d;";


     // Test files that have to fail the test.
    private static final String failTest = "src/donut/sample/testfiles/grammartests/failTest.donut";
    private static final String emptyTest = "src/donut/sample/testfiles/grammartests/totallyEmpty.donut";


     // Actual tests.

    @Test
    public void testArrayDeclaration() {
        verify(runTest(format(ArrayDeclaration), false));
        verify(runTest(format(ArrayOfArraysDeclaration), false));
    }

    @Test
    public void testCharacterDeclaration() {
        verify(runTest(format(CharacterDeclaration), false));
        verify(runTest(format(CharacterDeclaration2), false));
    }

    @Test
    public void testBooleanDeclaration() {
        verify(runTest(format(BooleanDeclaration), false));
        verify(runTest(format(BooleanDeclaration2), false));
    }

    @Test
    public void testIntegerDeclaration() {
        verify(runTest(format(IntegerDeclaration), false));
        verify(runTest(format(IntegerDeclaration2), false));
    }

    @Test
    public void testEmptyFile() {
        verify(runTest(EmtpyFile, true));
    }

    @Test
    public void testIfStatementsFile() {
        verify(runTest(IfStatementsFile, true));
    }

    @Test
    public void testMargeFile() {
        verify(runTest(MargeFile, true));
    }

    @Test
    public void testNumberOperatorsFile() {
        verify(runTest(NumberOperatorsFile, true));
    }

    @Test
    public void testNumbersFile() {
        verify(runTest(NumbersFile, true));
    }

    @Test
    public void testReactionFile() {
        verify(runTest(ReactionFile, true));
    }

    @Test
    public void testReactionOperatorsFile() {
        verify(runTest(ReactionOperatorsFile, true));
    }

    @Test
    public void testFail() {
        fails(runTest(failTest, true), 8);
        fails(runTest(emptyTest, true), 1);
    }


    /**
     * Verifies result
     * @param list - List with errors from the errorlistener.
     */
    public void verify(List<Error> list) {
        Assert.assertTrue(list.size() == 0);
    }

    /**
     * Checks whether list contains the expected number of errors.
     * @param list - List with errors from errorlistener.
     * @param expectedErrors - Expected errors in list.
     */
    public void fails(List<Error> list, int expectedErrors) {
        Assert.assertEquals(expectedErrors, list.size());
    }

    /**
     * Puts the testStatement between the begin line and end line of a donut file.
     * @param testStatement - statement to put between begin- and endline of donut file.
     * @return Correct donutfile.
     */
    public String format(String testStatement) {
        String beginFile = "Alright brain... it's all up to you {";
        String endFile = "} I hope I didn\'t brain my damage.";
        return beginFile + testStatement + endFile;
    }


    /**
     * Runs test with given file.
     * @param text - filename or the string to parse.
     * @param textIsFile - true if text is a file location, false if text is the text to parse.
     * @return
     */
    public List<Error> runTest(String text, boolean textIsFile) {
        CharStream stream = null;
        try {
            if (textIsFile) {
                stream = new ANTLRInputStream(new FileInputStream(new File(text)));
            } else {
                stream = new ANTLRInputStream(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
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