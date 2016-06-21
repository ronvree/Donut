package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Load MemAddr Reg] instruction from sprockell splitted into MemAddr - Deref Reg
 *  And MemAddr - Addr Int (see LoadI.java).
 */

public class LoadAI extends LocalInstruction {

    private Reg registerAddress;
    private Reg inRegister;

    /** LoadAI instruction for sprockell type: MemAddr -> Deref Reg */
    public LoadAI(Reg registerAddress, Reg inRegister) {
        this.registerAddress = registerAddress;
        this.inRegister = inRegister;
    }

    public Reg getRegisterAddress() {
        return registerAddress;
    }

    public Reg getInRegister() {
        return inRegister;
    }
}
