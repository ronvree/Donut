package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Store Reg MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class StoreI extends LocalInstruction {

    private Reg register;
    private int immediateValue;

    /**
     * StoreI instruction for sprockell type: MemAddr -> Immvalue Int
     */
    public StoreI(Reg register, int immediateValue) {
        this.register = register;
        this.immediateValue = immediateValue;
    }

    public Reg getRegister() {
        return register;
    }

    public int getImmediateValue() {
        return immediateValue;
    }
}
