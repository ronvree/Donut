package donut;

import donut.spril.Reg;

import java.util.Stack;

/**
 * Ulates registers
 */
public class RegUlator {

    private Stack<Reg> regStack;

    public RegUlator(int size) {
        this.regStack = new Stack<>();
        for (int i = size; i > 0; i--)  {
            regStack.push(new Reg("(reg" + i + ")"));
        }
    }

    public Reg get() throws NoRegException {
        if (regStack.size() > 0)   {
            return this.regStack.pop();
        } else {
            throw new NoRegException();
        }
    }

    public void add(Reg reg)    {
        this.regStack.push(reg);
    }

    public class NoRegException extends Exception {

    }

}
