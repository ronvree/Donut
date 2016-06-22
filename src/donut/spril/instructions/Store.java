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

public class Store extends LocalInstruction {

    private Reg atRegister;
    private Reg toStoreRegister;
    private String instruction = "Store";

    /** StoreAI instruction for sprockell type: MemAddr -> IndAddr Reg */
    public Store(Reg toStoreRegister, Reg atRegister) {
        this.atRegister = atRegister;
        this.toStoreRegister = toStoreRegister;
    }

    public Reg getAtRegister() {
        return atRegister;
    }

    public Reg getToStoreRegister() {
        return toStoreRegister;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(toStoreRegister.toString())
                .append(" ")
                .append("(IndAddr " + atRegister.toString() + ")")
        ;
        return builder.toString();
    }
}
