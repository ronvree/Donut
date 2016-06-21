package donut.spril;

/**
 * Created by Ron on 21-6-2016.
 */
public class Operator {

    private final String name;

    public static final Operator ADD = new Operator("Add");
    public static final Operator SUB = new Operator("Sub");
    public static final Operator MUL = new Operator("Mul");
    public static final Operator DIV = new Operator("Div");
    public static final Operator MOD = new Operator("Mod");
    public static final Operator EQUAL = new Operator("Equal");
    public static final Operator NEQ = new Operator("NEq");
    public static final Operator GT = new Operator("Gt");
    public static final Operator GTE = new Operator("GtE");
    public static final Operator LT = new Operator("Lt");
    public static final Operator LTE = new Operator("LtE");
    public static final Operator AND = new Operator("And");
    public static final Operator OR = new Operator("Or");
    public static final Operator XOR = new Operator("Xor");
    public static final Operator LSHIFT = new Operator("LShift");
    public static final Operator RSHIFT = new Operator("RShift");


    private Operator(String name)  {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString()    {
        return this.name;
    }

}
