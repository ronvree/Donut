package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Store extends LocalInstruction {

    private int directAddress;
    private Reg registerAddress;
    private boolean isDirect;

    private Reg toStore;


    /** Store instruction for sprockel type: MemAddr -> Addr Int */
    public Store(Reg toStore, int address) {
        this.directAddress = address;
        this.registerAddress = null;
        this.isDirect = true;

        this.toStore = toStore;
    }

    /** Store instruction for sprockel type: MemAddr -> Deref Reg */
    public Store(Reg toStore, Reg address) {
        this.directAddress = -1;
        this.registerAddress = address;
        this.isDirect = false;

        this.toStore = toStore;
    }

    public int getDirectAddress() {
        return directAddress;
    }

    public Reg getRegisterAddress() {
        return registerAddress;
    }

    public boolean isDirect() {
        return isDirect;
    }

    public Reg getToStore() {
        return toStore;
    }
}
