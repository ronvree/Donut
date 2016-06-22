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

public class Write extends SystemInstruction {

    private Reg sourceRegister;
    private Reg memoryRegister;
    private String instruction = "Write";

    /** Write instruction for sprockell for type: MemAddr - IndAddr Reg */
    public Write(Reg sourceRegister, Reg memoryRegister) {
        this.sourceRegister = sourceRegister;
        this.memoryRegister = memoryRegister;
    }

    public Reg getSourceRegister() {
        return sourceRegister;
    }

    public Reg getMemoryRegister() {
        return memoryRegister;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(sourceRegister.toString())
                .append(" ")
                .append("(IndAddr " + memoryRegister.toString() + ")")
        ;
        return builder.toString();
    }
}
