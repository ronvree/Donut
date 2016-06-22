package donut.spril.instructions;

/**
 * Created by Gijs on 21-Jun-16.
 */

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * [Load MemAddr Reg] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class LoadAI extends LocalInstruction {

    private int directAddress;
    private Reg register;
    private String instruction = "Load";

    /** LoadAI instruction for sprockell Load instruction with type: MemAddr -> DirAddr Int */
    public LoadAI(int directAddress, Reg register) {
        this.directAddress = directAddress;
        this.register = register;
    }

    public int getDirectAddress() {
        return directAddress;
    }

    public Reg getRegister() {
        return register;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(directAddress)
                .append(" ")
                .append(register.toString())
        ;
        return builder.toString();
    }


}
