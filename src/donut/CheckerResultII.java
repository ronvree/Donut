package donut;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

/**
 * Created by Ron on 23-6-2016.
 */
public class CheckerResultII {

    /** Mapping from expressions to types. */
    private final ParseTreeProperty<Type> types = new ParseTreeProperty<>();
    /** Mapping from variables to coordinates. */
    private final ParseTreeProperty<Integer> offsets = new ParseTreeProperty<>();
    /** Store for all variables if they shored be stored in shared or local memory */
    private final ParseTreeProperty<Boolean> shared = new ParseTreeProperty<>();

    /** Adds an association from a parse tree node containing a
     * variable reference to the offset
     * of that variable. */
    public void setOffset(ParseTree node, int offset) {
        this.offsets.put(node, offset);
    }

    /** Returns the declaration offset of the variable
     * accessed in a given parse tree node. */
    public int getOffset(ParseTree node) {
        return this.offsets.get(node);
    }

    /** Adds an association from a parse tree expression, type,
     * or assignment target node to the corresponding (inferred) type. */
    public void setType(ParseTree node, Type type) {
        this.types.put(node, type);
    }

    /** Returns the type associated with a given parse tree node. */
    public Type getType(ParseTree node) {
        return this.types.get(node);
    }

    /** Returns whether the variable is stored in shared memory */
    public boolean isShared(ParseTree node) { return this.shared.get(node); }

}
