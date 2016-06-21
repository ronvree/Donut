package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class EndProg extends LocalInstruction {

    private String instruction = "EndProg";

    /** EndProg instruction for sprockell. */
    public EndProg() {
        //Empty by design.
    }

    @Override
    public String toString() {
        return instruction;
    }
}
