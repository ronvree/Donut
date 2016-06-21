package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Const extends LocalInstruction {
    private int value;
    private Reg register;

    public Const(int value, Reg register) {
        this.value = value;
        this.register = register;
    }

    public int getValue() {
        return value;
    }

    public Reg getRegister() {
        return register;
    }
}
