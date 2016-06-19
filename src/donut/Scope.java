package donut;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ron on 19-6-2016.
 */
public class Scope {

    private int size;
    private Map<String, Type> types;
    private Map<String, Integer> offsets;

    public Scope()  {
        this.size = 0;
        this.types = new LinkedHashMap<>();
        this.offsets = new LinkedHashMap<>();

    }

    public boolean put(String id, Type type)   {
        boolean result = !types.containsKey(id);
        if (result)   {
            types.put(id, type);
            offsets.put(id, size);
            size += type.size();
        }
        return result;
    }

    public boolean contains(String id)  {
        return types.containsKey(id);
    }

    public Type getType(String id)  {
        return types.get(id);
    }

    public int getOffset(String id) {
        return offsets.get(id);
    }


}
