package donut.spril.instructions;

import donut.spril.LocalInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Compute extends LocalInstruction{

    private Operator operator;
    private Reg term1Reg;
    private Reg term2Reg;
    private Reg resultReg;

    public Compute(Operator op, Reg term1, Reg term2, Reg result) {
        this.operator = op;
        this.term1Reg = term1;
        this.term2Reg = term2;
        this.resultReg = result;
    }

    public Operator getOperator() {
        return operator;
    }

    public Reg getTerm2Reg() {
        return term2Reg;
    }

    public Reg getTerm1Reg() {
        return term1Reg;
    }

    public Reg getResultReg() {
        return resultReg;
    }
}
