package donut.spril;

/**
 * Created by Ron on 21-6-2016.
 */
public class Int extends Operand {

    private int value;

    public Int(int value)    {
        this.value = value;
    }

    public int getValue()   { return this.value; }

    @Override
    public String toString()    {
        return  String.valueOf(value);
    }

}
