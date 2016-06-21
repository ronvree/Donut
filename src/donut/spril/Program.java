package donut.spril;

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






}
