package donut;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Ron on 19-6-2016.
 */
public class SymbolTableII {

    /**
     Stores the scopes needed for type checking
     */
    private Stack<Scope> scopes;
    /**
     * Current size of the scopes (in bytes)
     */
    private int size;

    public SymbolTableII()    {
        this.scopes = new Stack<>();
        this.scopes.push(new Scope());
        this.size = 1;
    }

    /**
     * Open a new scope
     */
    public void openScope() {
        this.scopes.push(this.scopes.peek().deepCopy());
    }

    /**
     * Open a new scope where only shared variables are visible
     */
    public void openSharedScope() { this.scopes.push(this.scopes.peek().sharedCopy()); }

    /**
     * Close the current scope. Final scope cannot be closed
     */
    public void closeScope()    {
        if (scopes.size() > 1)   {
            this.scopes.pop();
        } else {
            throw new RuntimeException("Can't pop top-level scope!");
        }
    }

    /**
     * Put the variable id and its type in the current scope.
     * @param id -- Variable ID
     * @param type -- Variable type
     * @return whether the ID did not already occur in the current scope
     */
    public boolean put(String id, Type type, boolean shared)   {
        boolean result = !scopes.peek().contains(id);
        if (result)   {
            scopes.peek().put(id, type, shared, this.size);
            this.size += type.size();
        }
        return result;
    }

    /**
     * Tests if a given id is declared in the current scope
     */
    public boolean contains(String id)  {
        return this.scopes.peek().contains(id);
    }

    /**
     * Give the current scope
     */
    public Scope getCurrentScope()  {
        return this.scopes.peek();
    }

    /**
     * Store the types in this scope
     */
    public class Scope {

        /** Bind the types to ID */
        private final Map<String, Type> types;
        /** Bind offsets to the ID */
        private final Map<String, Integer> offsets;
        /** Indicate for all ID's whether they are stored in shared or local memory */
        private final Map<String, Boolean> shared;

        public Scope()  {
            this.offsets = new HashMap<>();
            this.types = new HashMap<>();
            this.shared = new HashMap<>();
        }

        /** Declare a new variable */
        public void put(String id, Type type, boolean shared, int offset)    {
            this.types.put(id, type);
            this.offsets.put(id, offset);
            this.shared.put(id, shared);
        }

        /**
         * Deepcopy the scope
         */
        public Scope deepCopy() {
            Scope copy = new Scope();
            for (String id : this.types.keySet())  {
                copy.put(id, this.types.get(id), this.shared.get(id), this.offsets.get(id));
            }
            return copy;
        }

        /**
         * Makes a new scope where only shared variables are visible
         */
        public Scope sharedCopy()   {
            Scope copy = new Scope();
            for (String id : this.types.keySet())  {
                if (this.shared.get(id))   {
                    copy.put(id, types.get(id), shared.get(id), offsets.get(id));
                }
            }
            return copy;
        }

        /**
         * Returns if the variable is declared in any scope
         */
        public boolean contains(String id)  {
            return types.containsKey(id);
        }

        /**
         * Return the type of the variable
         */
        public Type getType(String id)   {
            return this.types.get(id);
        }

        /**
         * Return the offset of the variable
         */
        public int getOffset(String id) { return this.offsets.get(id); }

        /**
         * Return if the variable is stored in shared memory
         */
        public boolean isShared(String id) { return this.shared.get(id); }

    }


}
