package donut.spril.instructions;

import donut.spril.Reg;
import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Read MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class Read extends SystemInstruction {

    private Reg register;
    private String instruction = "ReadInstr";

    /** Read instruction for sprockell type: MemAddr -> IndAddr Reg */
    public Read(Reg register) {
        this.register = register;
    }

    public Reg getRegister() {
        return register;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append("(IndAddr " + register.toString() + ")")
        ;
        return builder.toString();
    }
}
