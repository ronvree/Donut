package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Branch Reg Target] instruction from sprockell splitted into Target - Abs/Rel Int
 *  and Target - Ind Reg (see BranchAI.java).
 */

public class BranchI extends LocalInstruction {

    private Reg cmpRegister;
    private int absoluteAddress;
    private int relativeAddress;
    private boolean isAbsolute;

    private String instruction = "Branch";


    /** BranchI instruction for sprockell type: Target -> Abs/Rel Int */
    public BranchI(Reg cmpRegister, int address, boolean isAbsolute) {
        this.cmpRegister = cmpRegister;
        if (isAbsolute) {
            this.absoluteAddress = address;
            this.isAbsolute = true;
        } else {
            this.relativeAddress = address;
            this.isAbsolute = false;
        }
    }

    public int getAddress() {
        if (isAbsolute) {
            return absoluteAddress;
        }
        return relativeAddress;
    }

    public Reg getCmpRegister() {
        return cmpRegister;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(cmpRegister)
                .append(" ");

        if (isAbsolute) {
            builder.append("(Abs (" + absoluteAddress + "))");
        } else {
            builder.append("(Rel (" + relativeAddress + "))");
        }

        return builder.toString();
    }

}
