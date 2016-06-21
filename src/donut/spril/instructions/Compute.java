package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Operator;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class Compute extends LocalInstruction{

    private Operator operator;
    private Reg term1Reg;
    private Reg term2Reg;
    private Reg resultReg;
    private String instruction = "Compute";

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(instruction)
                .append(" ")
                .append(operator.toString())
                .append(" ")
                .append(term1Reg)
                .append(" ")
                .append(term2Reg)
                .append(" ")
                .append(resultReg)
        ;
        return builder.toString();
    }

}
