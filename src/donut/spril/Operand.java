package donut.spril;

/**
 * Created by Ron on 21-6-2016.
 */
public abstract class Operand {

    private final Type type;

    protected Operand(Type type) {
        this.type = type;
    }

    /** Returns the type of this operand. */
    public Type getType() {
        return this.type;
    }

    /** Enumeration of all available operand types. */
    public static enum Type {
        REG,
        INT,
        MEMADDR,
        OPERATOR,
        TARGET,
        STR;
    }
}
