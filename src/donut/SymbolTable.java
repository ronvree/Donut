package donut;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Ron on 19-6-2016.
 */
public class SymbolTable {

    /**
       Stores the scopes needed for type checking
     */
    private Stack<Scope> scopes;
    /**
     * Current size of the scopes (in bytes)
     */
    private int size;

    public SymbolTable()    {
        this.scopes = new Stack<>();
        this.scopes.push(new Scope());
        this.size = 0;
    }

    /**
     * Open a new scope
     */
    public void openScope() {
        this.scopes.push(this.scopes.peek().deepCopy());
    }

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
    public boolean put(String id, Type type)   {
        boolean result = !scopes.peek().contains(id);
        if (result)   {
            scopes.peek().put(id, type, this.size);
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

        /**
         * Map the ID's to types
         */
        private final Map<String, Type> types;
        private final Map<String, Integer> offsets;

        public Scope()  {
            this.offsets = new HashMap<>();
            this.types = new HashMap<>();
        }

        public void put(String id, Type type, int offset)    {
            types.put(id, type);
            offsets.put(id, offset);
        }

        public Scope deepCopy() {
            Scope copy = new Scope();
            for (String id : this.types.keySet())  {
                copy.put(id, this.types.get(id), this.offsets.get(id));
            }
            return copy;
        }

        public boolean contains(String id)  {
            return types.containsKey(id);
        }

        public Type getType(String id)   {
            return this.types.get(id);
        }

        public int getOffset(String id) { return this.offsets.get(id); }


    }


}
