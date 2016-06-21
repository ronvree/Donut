package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Branch extends LocalInstruction {

    private Reg cmpRegister;

    private int targetAbsInt;
    private int targetRelInt;
    private Reg targetRegister;
    private boolean isAbsolute;

    /** Branch instruction for sprockel type: Target -> Abs Int and Target -> Rel Int.
     *  Indicated by boolean isAbsoluteTarget.
     */
    public Branch(Reg cmpRegister, int targetInt, boolean isAbsoluteTarget) {
        this.cmpRegister = cmpRegister;
        this.targetRegister = null;
        if (isAbsoluteTarget) {
            this.targetAbsInt = targetInt;
            this.targetRelInt = -1;
            this.isAbsolute = true;
        } else {
            this.targetAbsInt = -1;
            this.targetRelInt = targetInt;
            this.isAbsolute = false;
        }
    }

    /** Branch instruction for sprockel type: Target -> Ind Reg */
    public Branch (Reg cmpRegister, Reg targetRegister) {
        this.cmpRegister = cmpRegister;
        this.targetRegister = targetRegister;
    }

    public Reg getCmpRegister() {
        return cmpRegister;
    }

    public int getTargetAbsInt() {

        return targetAbsInt;
    }

    public int getTargetRelInt() {
        return targetRelInt;
    }

    public Reg getTargetRegister() {
        return targetRegister;
    }

    public boolean isAbsolute() {
        return isAbsolute;
    }
}
