package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Jump extends LocalInstruction {

    private int targetAbsInt;
    private int targetRelInt;
    private Reg targetRegister;
    private boolean isAbsolute;

    /** Jump instruction for sprockel type: Target -> Abs Int and Target -> Rel Int.
     *  Indicated by boolean isAbsoluteTarget.
     */
    public Jump(int targetInt, boolean isAbsoluteTarget) {
        targetRegister = null;
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


    public Jump(Reg targetRegister) {
        this.targetAbsInt = -1;
        this.targetRelInt = -1;
        this.targetRegister = targetRegister;
        this.isAbsolute = false;
    }
}
