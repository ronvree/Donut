package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Load extends LocalInstruction {

    /** MemAddr from sprockel splitted into integer address and
     *  register address, with a boolean indicating which one to use. */
    private int directAddress;
    private Reg registerAddress;
    private boolean isDirect;

    private Reg inRegister;

    /** Load instruction for sprockel type: MemAddr -> Addr Int */
    private Load(int directAddress, Reg register) {
        this.directAddress = directAddress;
        this.registerAddress = null;
        this.inRegister = register;
        this.isDirect = true;
    }


    /** Load instruction for sprockel type: MemAddr -> Deref Reg */
    private Load(Reg registerAddress, Reg inRegister) {
        this.directAddress = -1;
        this.registerAddress = registerAddress;
        this.isDirect = false;

        this.inRegister = inRegister;
    }

    public int getDirectAddress() {
        if (isDirect) {
            return address;
        }
        throw new ;
    }

    public Reg getRegisterAddress() {
        return register;
    }
}
