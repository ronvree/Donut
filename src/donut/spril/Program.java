package donut.spril;

import donut.CheckerResult;
import donut.Generator;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ron on 21-6-2016.
 *
 * A sequence of Spril instructions
 */
public class Program {

    /** Instructions */
    private List<Instruction> instructions;

    public Program()    {
        this.instructions = new ArrayList<>();
    }



    /** Add an instruction */
    public void add(Instruction instr)  {
        this.instructions.add(instr);
    }



    public void writeFile(String filename) {
        StringBuffer buffer = new StringBuffer();
        for (Instruction i : instructions) {
            buffer.append(i.toString() + "\n");
        }
        try {
            Files.write(Paths.get(filename), buffer.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printInstructions() {
        for (Instruction i : instructions) {
            System.out.println(i.toString());
        }
    }


}
