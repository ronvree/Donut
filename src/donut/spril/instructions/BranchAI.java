package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */

/**
 * [Branch Reg Target] instruction from sprockell splitted into Target - Ind Reg
 *  and Target - Abs/Rel Int (see BranchI.java).
 */

public class BranchAI extends LocalInstruction {

    private Reg cmpRegister;
    private Reg targetRegister;

    /** Branch instruction for sprockell type: Target -> Ind Reg */
    public BranchAI(Reg cmpRegister, Reg targetRegister) {
        this.cmpRegister = cmpRegister;
        this.targetRegister = targetRegister;
    }

    public Reg getCmpRegister() {
        return cmpRegister;
    }

    public Reg getTargetRegister() {
        return targetRegister;
    }
}
