package donut;

import donut.checkers.CheckerII;
import donut.errors.Error;
import donut.errors.TypeError;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ron on 23-6-2016.
 */
public class CheckerIITest {

    private static final String BASE_DIR = "src/donut/sample/testfiles/checker/";
    private static final String EXT = ".donut";

    @Test
    public void testDeclarations()  {
        DonutParser.ProgramContext prog = parse("declarations");
        List<Error> errors = listErrors(prog);
        final int count = 8;
        Assert.assertEquals(count, errors.size());
    }

    @Test
    public void testTypes() {
        DonutParser.ProgramContext prog = parse("types");
        List<Error> errors = listErrors(prog);
        final int count = 23;
        Assert.assertEquals(count, errors.size());
        for (int i = 0; i < count; i++)  {
            Error error = errors.get(i);
            Assert.assertEquals(true, error instanceof TypeError);
        }
    }

    @Test
    public void testArrays()    {
        DonutParser.ProgramContext prog = parse("arrays");
        List<Error> errors = listErrors(prog);
        final int count = 3;
        Assert.assertEquals(count, errors.size());

        for (Error error : errors)  {
            System.out.println(error);
        }

    }

    @Test
    public void testConcurrent()   {
        DonutParser.ProgramContext prog = parse("concurrent");
        List<Error> errors = listErrors(prog);
        final int count = 2;
        Assert.assertEquals(count, errors.size());

        for (Error error : errors)  {
            System.out.println(error);
        }
    }

    private List<Error> listErrors(DonutParser.ProgramContext prog) {
        ParseTreeWalker walker = new ParseTreeWalker();
        CheckerII checker = new CheckerII();
        walker.walk(checker, prog);
        return checker.getErrors();
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
        DonutParser.ProgramContext prog = parser.program();
        return prog;
    }




}