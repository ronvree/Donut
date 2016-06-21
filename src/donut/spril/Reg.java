package donut.spril;

/**
 * Created by Ron on 21-6-2016.
 */
public class Reg extends Operand {

    private String name;

    public Reg(String name) {
        this.name = "Reg " + name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString()    {
        return this.name;
    }

}
