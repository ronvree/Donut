package donut.spril.instructions;

import donut.spril.Reg;
import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [TestAndSet MemAddr] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */

public class TestAndSet extends SystemInstruction {

    private Reg register;
    private String instruction = "TestAndSet";

    /** TestAndSet instruction for sprockell for type: MemAddr - IndAddr Reg */
    public TestAndSet(Reg register) {
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
                .append(register.toString())
        ;
        return builder.toString();
    }
}
