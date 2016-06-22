package donut.spril.instructions;

import donut.spril.Reg;
import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Write Reg MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class WriteAI extends SystemInstruction {

    private Reg sourceRegister;
    private int directAddress;
    private String instruction = "Write";

    /** WriteAI instruction for sprockell for type: MemAddr - DirAddr Int */
    public WriteAI(Reg sourceRegister, int directAddress) {
        this.sourceRegister = sourceRegister;
        this.directAddress = directAddress;
    }

    public Reg getSourceRegister() {
        return sourceRegister;
    }

    public int getDirectAddress() {
        return directAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(sourceRegister.toString())
                .append(" ")
                .append("(DirAddr " + directAddress + ")")
        ;
        return builder.toString();
    }
}
