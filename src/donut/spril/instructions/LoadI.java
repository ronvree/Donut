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
    private int immediateValue;
    private boolean isAddress;
    private Reg inRegister;

    /** LoadI instruction for sprockell type: MemAddr -> DirAddr Int
     *  and MemAddr -> ImmValue Int */
    private LoadI(int addressOrValue, Reg register, boolean isAddress) {
        if(isAddress) {
            this.directAddress = addressOrValue;
            this.isAddress = true;
        } else {
            immediateValue = addressOrValue;
            this.isAddress = false;
        }
        this.inRegister = register;
    }

    public int getAddress() {
        if (isAddress) {
            return this.directAddress;
        }
        return immediateValue;
    }

    public Reg getInRegister() {
        return inRegister;
    }
}
