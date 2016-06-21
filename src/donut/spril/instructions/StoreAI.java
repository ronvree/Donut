package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Store Reg MemAddr] instruction from sprockell splitted into MemAddr - Deref Reg
 *  And MemAddr - Addr Int (see StoreI.java).
 */

public class StoreAI extends LocalInstruction {

    private Reg atRegister;
    private Reg toStoreRegister;

    /** StoreAI instruction for sprockell type: MemAddr -> IndAddr Reg */
    public StoreAI(Reg toStoreRegister, Reg atRegister) {
        this.atRegister = atRegister;
        this.toStoreRegister = toStoreRegister;
    }

    public Reg getAtRegister() {
        return atRegister;
    }

    public Reg getToStoreRegister() {
        return toStoreRegister;
    }
}
