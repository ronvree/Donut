package donut.spril.instructions;

import donut.spril.Reg;
import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

public class Receive extends SystemInstruction {

    private Reg register;

    /** Receive instruction for sprockell */
    public Receive(Reg register) {
        this.register = register;
    }

    public Reg getRegister() {
        return register;
    }
}
