package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Store Reg MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int / ImmValue Int
 *  And MemAddr - Ind Reg (see StoreAI.java).
 */

public class StoreI extends LocalInstruction {

    private Reg toStoreAddress;
    private int directAddress;
    private int immediateValue;
    private boolean isAddress;

    /** Store instruction for sprockell type: MemAddr -> Addr Int
     *  and MemAddr -> Immvalue Int */
    public StoreI(Reg toStoreAddress, int addressOrValue, boolean isAddress) {
        this.toStoreAddress = toStoreAddress;
        if (isAddress) {
            this.directAddress = addressOrValue;
            this.isAddress = true;
        } else {
            this.immediateValue = addressOrValue;
            this.isAddress = false;
        }
    }

    public Reg getToStoreAddress() {
        return toStoreAddress;
    }

    public int getAddress() {
        if (isAddress) {
            return directAddress;
        }
        return immediateValue;
    }
}
