package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

public class Push extends LocalInstruction {

    private Reg storeRegister;

    /** Push instruction for sprockell */
    public Push(Reg storeRegister) {
        this.storeRegister = storeRegister;
    }

    public Reg getStoreRegister() {
        return storeRegister;
    }
}
