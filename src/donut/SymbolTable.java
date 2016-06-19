package donut;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Ron on 19-6-2016.
 */
public class SymbolTable {

    private Stack<Scope> scopes;
    private int size;
    private Map<String, Stack<Integer>> offsets;

    public SymbolTable()    {
        this.scopes = new Stack<>();
        this.scopes.push(new Scope());
        this.offsets = new HashMap<>();
        this.size = 0;
    }

    public void openScope() {
        this.scopes.push(this.scopes.peek().deepCopy());
    }

    public void closeScope()    {
        if (scopes.size() > 1)   {
            this.scopes.pop();
        } else {
            throw new RuntimeException("Can't pop top-level scope!");
        }
    }

    public boolean put(String id, Type type)   {
        boolean result = !scopes.peek().contains(id);
        if (result)   {
            scopes.peek().put(id, type);
            storeOffset(id, type);

        }
        return result;
    }

    public boolean contains(String id)  {
        return this.scopes.peek().contains(id);
    }

    public Scope getCurrentScope()  {
        return this.scopes.peek();
    }

    public Map<String, Stack<Integer>> getOffsets() {
        return this.offsets;
    }

    private void storeOffset(String id, Type type)  {
        if (!this.offsets.keySet().contains(id))   {
            this.offsets.put(id, new Stack<>());
        }
        this.offsets.get(id).push(this.size);
        this.size += type.size();

    }

    private class Scope {

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
