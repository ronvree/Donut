package donut.spril.instructions;

import donut.spril.SystemInstruction;

/**
 * Created by Gijs on 21-Jun-16.
 */
public class ReadI extends SystemInstruction {

    private int directAddress;
    private int immediateValue;
    private boolean isAddress;

    public ReadI(int addressOrValue, boolean isAddress) {
        this.isAddress = isAddress;
        if (isAddress) {
            this.directAddress = addressOrValue;
        } else {
            this.immediateValue = addressOrValue;
        }
    }

    public int getAddress() {
        if(isAddress) {
            return directAddress;
        }
        return immediateValue;
    }
}
