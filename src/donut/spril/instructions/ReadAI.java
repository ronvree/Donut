package donut.spril.instructions;

import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Read MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class ReadAI extends SystemInstruction {

    private int directAddress;

    /** ReadAI instruction for sprockell type: MemAddr -> DirAddr Int */
    public ReadAI(int directAddress) {
        this.directAddress = directAddress;
    }

    public int getDirectAddress() {
        return directAddress;
    }
}
