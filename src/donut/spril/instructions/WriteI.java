package donut.spril.instructions;

import donut.spril.Reg;
import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Write Reg MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class WriteI extends SystemInstruction {

    private Reg sourceRegister;
    private int immediateValue;

    /** WriteI instruction for sprockell for type: MemAddr - ImmValue Int */
    public WriteI(Reg sourceRegister, int immediateValue) {
        this.sourceRegister = sourceRegister;
        this.immediateValue = immediateValue;
    }

    public Reg getSourceRegister() {
        return sourceRegister;
    }

    public int getImmediateValue() {
        return immediateValue;
    }
}
