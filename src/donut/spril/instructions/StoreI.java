package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Store Reg MemAddr] instruction from sprockell splitted into MemAddr - Addr Int
 *  And MemAddr - Deref Reg (see StoreAI.java).
 */

public class StoreI extends LocalInstruction {

    private Reg toStoreAddress;
    private int inAddress;

    /** Store instruction for sprockell type: MemAddr -> Addr Int */
    public StoreI(Reg toStoreAddress, int address) {
        this.toStoreAddress = toStoreAddress;
        this.inAddress = address;
    }

    public Reg getToStoreAddress() {
        return toStoreAddress;
    }

    public int getInAddress() {
        return inAddress;
    }
}
