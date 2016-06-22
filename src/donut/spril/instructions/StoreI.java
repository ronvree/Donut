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
    private String instruction = "Store";

    /**
     * StoreI instruction for sprockell type: MemAddr -> Immvalue Int
     */

    @Deprecated
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

    @Override @Deprecated
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(register.toString())
                .append(" ")
                .append(immediateValue)
        ;
        return builder.toString();
    }
}
