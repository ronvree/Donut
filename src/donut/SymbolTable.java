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
    /**
     * Stores the offsets of variables. The stacks allow for offset declaration in multiple scopes
     */
    private Map<String, Stack<Integer>> offsets;

    public SymbolTable()    {
        this.scopes = new Stack<>();
        this.scopes.push(new Scope());
        this.offsets = new HashMap<>();
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
            scopes.peek().put(id, type);
            storeOffset(id, type);

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
     * Give the overview of variable ID's and declaration offsets
     */
    public Map<String, Stack<Integer>> getOffsets() {
        return this.offsets;
    }

    /**
     * Store the offset and update the total size
     */
    private void storeOffset(String id, Type type)  {
        if (!this.offsets.keySet().contains(id))   {
            this.offsets.put(id, new Stack<>());
        }
        this.offsets.get(id).push(this.size);
        this.size += type.size();

    }

    /**
     * Store the types in this scope
     */
    private class Scope {

        /**
         * Map the ID's to types
         */
        private final Map<String, Type> types;

        public Scope()  {
            this.types = new HashMap<>();
        }

        public void put(String id, Type type)    {
            types.put(id, type);
        }

        public Scope deepCopy() {
            Scope copy = new Scope();
            for (String id : this.types.keySet())  {
                copy.put(id, this.types.get(id));
            }
            return copy;
        }

        public boolean contains(String id)  {
            return types.containsKey(id);
        }

        public Type getType(String id)   {
            return this.types.get(id);
        }


    }


}
