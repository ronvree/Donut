package donut.spril.instructions;

import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Read MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class ReadI extends SystemInstruction {

    private int immediateValue;
    private String instruction = "ReadInstr";

    /** ReadI instruction for sprockell type: MemAddr -> ImmValue Int */
    @Deprecated
    public ReadI(int immediateValue) {
        this.immediateValue = immediateValue;
    }

    public int getImmediateValue() {
        return immediateValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append("(ImmValue " + immediateValue + ")")
        ;
        return builder.toString();
    }
}
