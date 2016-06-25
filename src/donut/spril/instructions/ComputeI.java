package donut.spril.instructions;

import donut.spril.LocalInstruction;
import donut.spril.Operator;
import donut.spril.Reg;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class ComputeI extends LocalInstruction{

    private Operator operator;
    private int term1;
    private Reg term2Reg;
    private Reg resultReg;
    private String instruction = "Compute";

    public ComputeI(Operator op, int term1, Reg term2, Reg result) {
        this.operator = op;
        this.term1 = term1;
        this.term2Reg = term2;
        this.resultReg = result;
    }

    public Operator getOperator() {
        return operator;
    }

    public Reg getTerm2Reg() {
        return term2Reg;
    }

    public int getTerm1() {
        return term1;
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
                .append("(ImmValue (" + term1 +"))")
                .append(" ")
                .append(term2Reg)
                .append(" ")
                .append(resultReg)
        ;
        return builder.toString();
    }

}
