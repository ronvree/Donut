package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Jump Reg Target] instruction from sprockell splitted into Target - Ind Reg
 *  and Target - Abs/Rel Int (see JumpI.java).
 */

public class JumpAI extends LocalInstruction {

    private Reg targetRegister;
    private String instruction = "Jump";

    /** Jump instruction for sprockell type: Target -> Ind Reg */
    public JumpAI(Reg targetRegister) {
        this.targetRegister = targetRegister;
    }

    public Reg getTargetRegister() {
        return targetRegister;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(targetRegister)
        ;
        return builder.toString();
    }
}
