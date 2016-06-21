package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Load MemAddr Reg] instruction from sprockell splitted into MemAddr - DirAddr Int,
 *  MemAddr - IndAddr Reg and MemAddr - ImmValue Int.
 */
public class LoadI extends LocalInstruction {

    private int immediateValue;
    private Reg inRegister;
    private String instruction = "Load";

    /** LoadI instruction for sprockell type: MemAddr -> ImmValue Int */
    public LoadI(int immediateValue, Reg register) {
        this.immediateValue = immediateValue;
        this.inRegister = register;
    }

    public int getAddress() {
        return immediateValue;
    }

    public Reg getInRegister() {
        return inRegister;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(inRegister.toString())
                .append(" ")
                .append(immediateValue)
        ;
        return builder.toString();
    }
}
