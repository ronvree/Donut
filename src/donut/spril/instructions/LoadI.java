package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Load MemAddr Reg] instruction from sprockell splitted into MemAddr - Addr Int
 *  And MemAddr - Deref Reg (see LoadAI.java).
 */
public class LoadI extends LocalInstruction {

    private int directAddress;
    private Reg inRegister;

    /** LoadI instruction for sprockell type: MemAddr -> Addr Int */
    private LoadI(int directAddress, Reg register) {
        this.directAddress = directAddress;
        this.inRegister = register;
    }

    public int getDirectAddress() {
        return directAddress;
    }

    public Reg getInRegister() {
        return inRegister;
    }
}
