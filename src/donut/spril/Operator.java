package donut.spril;

/**
 * Created by Ron on 21-6-2016.
 */
public class Operator {

    private final String name;

    public Operator(Type type)   {
        switch (type)   {
            case ADD:
                this.name = "Add";
                break;
            case SUB:
                this.name = "Sub";
                break;
            case MUL:
                this.name = "Mul";
                break;
            case DIV:
                this.name = "Div";
                break;
            case MOD:
                this.name = "Mod";
                break;
            case EQUAL:
                this.name = "Equal";
                break;
            case NEQ:
                this.name = "NEq";
                break;
            case GT:
                this.name = "Gt";
                break;
            case GTE:
                this.name = "GtE";
                break;
            case LT:
                this.name = "Lt";
                break;
            case LTE:
                this.name = "LtE";
                break;
            case AND:
                this.name = "And";
                break;
            case OR:
                this.name = "Or";
                break;
            case XOR:
                this.name = "Xor";
                break;
            case LSHIFT:
                this.name = "LShift";
                break;
            case RSHIFT:
                this.name = "RShift";
                break;
            default:
                this.name = "NO SUCH OPERATOR";
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString()    {
        return this.name;
    }

    public enum Type    {
        ADD,
        SUB,
        MUL,
        DIV,
        MOD,
        EQUAL,
        NEQ,
        GT,
        GTE,
        LT,
        LTE,
        AND,
        OR,
        XOR,
        LSHIFT,
        RSHIFT
        ;

    }

}
