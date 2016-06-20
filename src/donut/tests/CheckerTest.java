package donut.tests;

import donut.Checker;
import donut.DonutLexer;
import donut.DonutParser;
import donut.errors.Error;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Ron on 20-6-2016.
 */
public class CheckerTest {

    private static final String BASE_DIR = "src/donut/sample/testfiles/checker/";
    private static final String EXT = ".donut";

    @Test
    public void testDeclarations()  {
        try {
            CharStream chars = new ANTLRInputStream(new FileReader(new File(BASE_DIR + "declarations" + EXT)));
            Lexer lexer = new DonutLexer(chars);
            TokenStream tokens = new CommonTokenStream(lexer);
            DonutParser parser = new DonutParser(tokens);
            DonutParser.ProgramContext prog = parser.program();

            ParseTreeWalker walker = new ParseTreeWalker();
            Checker checker = new Checker();
            walker.walk(checker, prog);

            for (Error error : checker.getErrors())  {
                System.out.println(error);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testWhileCondition()    {

    }


}
