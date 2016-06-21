package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Nop extends LocalInstruction {
    private String instruction = "Nop";

    /** Nop instruction for sprockell. */
    public Nop() {
        // empty by design.
    }

    @Override
    public String toString() {
        return instruction;
    }
}
