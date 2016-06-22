package donut.spril.instructions;

/**
 * Created by Gijs on 21-Jun-16.
 */

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * [Store Reg MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class StoreAI extends LocalInstruction {

    private Reg register;
    private int directAddress;
    private String instruction = "Store";

    /** StoreAI instruction for sprockell type: MemAddr -> DirAddr Int */
    public StoreAI(Reg register, int directAddress) {
        this.register = register;
        this.directAddress = directAddress;
    }

    public Reg getRegister() {
        return register;
    }

    public int getDirectAddress() {
        return directAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(register.toString())
                .append(" ")
                .append("(DirAddr " + directAddress + ")")
        ;
        return builder.toString();
    }
}
