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
    private String instruction = "Read";

    /** ReadAI instruction for sprockell type: MemAddr -> DirAddr Int */
    public ReadAI(int directAddress) {
        this.directAddress = directAddress;
    }

    public int getDirectAddress() {
        return directAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(directAddress)
        ;
        return builder.toString();
    }
}
