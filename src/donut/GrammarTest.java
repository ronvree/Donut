package donut;

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

    /**
     * Test files with a full program that have to pass the test.
     */
    private static final String EmtpyFile = "src/donut/sample/testFiles/GrammarTests/testEmpty.donut";
    private static final String IfStatementsFile = "src/donut/sample/testFiles/GrammarTests/testIfStatements.donut";
    private static final String MargeFile = "src/donut/sample/testFiles/GrammarTests/testMarge.donut";
    private static final String NumberOperatorsFile = "src/donut/sample/testFiles/GrammarTests/testNumberOperators.donut";
    private static final String NumbersFile = "src/donut/sample/testFiles/GrammarTests/testNumbers.donut";
    private static final String ReactionFile = "src/donut/sample/testFiles/GrammarTests/testReaction.donut";
    private static final String ReactionOperatorsFile = "src/donut/sample/testFiles/GrammarTests/testReactionOperators.donut";


    /**
     * Tests for certain parts of the code that have to pass the test.
     */
    private static final String IntegerDeclaration = "number n = 4;";
    private static final String IntegerDeclaration2 = "number j;";
    private static final String ArrayDeclaration = "bunchof number a = bunchof 6 number;";
    private static final String ArrayOfArraysDeclaration = "bunchof bunchof bunchof number a = bunchof 5 bunchof 4 bunchof 3 number;";
    private static final String CharacterDeclaration = "symbol c = \'%\';";
    private static final String CharacterDeclaration2 = "symbol c;";
    private static final String BooleanDeclaration = "reaction r = WOOHOO;";
    private static final String BooleanDeclaration2 = "reaction d;";

    /**
     * Test file with a number of errors in it.
     */

    private static final String failTest = "src/donut/sample/testFiles/GrammarTests/failTest.donut";




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
    public void testFail() {fails(runTest(failTest, true));}

    public void verify(List<Error> list) {
        Assert.assertTrue(list.size() == 0);
    }

    public void fails(List<Error> list) {
        for (Error e : list) {
            System.out.println(e.toString());
        }
    }


    public String format(String testStatement) {
        String beginFile = "Alright brain... it's all up to you {";
        String endFile = "} I hope I didn\'t brain my damage.";
        return beginFile + testStatement + endFile;
    }




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