package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Pop extends LocalInstruction {

    private Reg popRegister;
    private String instruction = "Pop";

    /** Pop instruction for sprockell */
    public Pop(Reg popRegister) {
        this.popRegister = popRegister;
    }

    public Reg getPopRegister() {
        return popRegister;
    }

    @Override
    public String toString() {
        return instruction;
    }
}
