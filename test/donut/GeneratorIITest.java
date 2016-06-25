package donut;

import donut.checkers.deprecated.Checker;
import donut.generators.deprecated.GeneratorII;
import donut.spril.Program;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Ron on 23-6-2016.
 */
public class GeneratorIITest {

    private static final String BASE_DIR = "src/donut/sample/";
    private static final String EXT = ".donut";

    @Test
    public void test()  {
        DonutParser.ProgramContext programContext = parse("Gauss");
        ParseTreeWalker walker = new ParseTreeWalker();
        Checker checker = new Checker();
        walker.walk(checker, programContext);

        GeneratorII generator = new GeneratorII();
        Program prog = generator.generate(programContext, checker.getResult());
        prog.printInstructions();
        prog.writeHaskellFile("testResult.hs");

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