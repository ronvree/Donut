package donut.spril;

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

    private String programName;

    /** Haskell file initialization  */
    private String INIT_FILE =
            "module Main where\n" +
            "\n" +
            "import BasicFunctions\n" +
            "import HardwareTypes\n" +
            "import Sprockell\n" +
            "import System\n" +
            "import Simulation\n" +
            "\n"
            ;

    public Program()    {
        this.instructions = new ArrayList<>();
    }



    /** Add an instruction */
    public void add(Instruction instr)  {
        this.instructions.add(instr);
    }

    public boolean replace(Instruction take, Instruction place)   {
        int index = instructions.indexOf(take);
        if (index == -1)   {
            return false;
        } else {
            this.instructions.set(index, place);
            return true;
        }
    }

    public String writeProgram(String programName) {
        this.programName = programName;
        StringBuffer buffer = new StringBuffer();
        buffer.append(programName + " :: [Instruction]\n")
                .append(programName + " = [");
        for (Instruction i : instructions) {
            buffer.append("    " + i.toString() + ",\n");
        }
        buffer.deleteCharAt(buffer.toString().length() - 2);
        buffer.append("    ]\n");
        return buffer.toString();
    }

    public String getProgramName() {
        return programName;
    }

    public int size() {
        return instructions.size();
    }

    public void printInstructions() {
        int line = 0;
        for (Instruction i : instructions) {
            System.out.println(line + ": " + i.toString());
            line++;
        }
    }
}
