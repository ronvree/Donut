package donut;

/**
 * Created by Ron on 19-6-2016.
 */
public abstract class Type {

    public static final ReactionType REACTION_TYPE = new ReactionType();
    public static final NumberType NUMBER_TYPE = new NumberType();
    public static final SymbolType SYMBOL_TYPE = new SymbolType();

    /**
     * Give the size of this data type in bytes
     */
    public abstract int size();

    /**
     * Reaction
     */
    public static class ReactionType extends Type   {

        private ReactionType()  {}

        @Override
        public int size() { // TODO
            return 0;
        }   // TODO

    }

    /**
     * Number
     */
    public static class NumberType extends Type {

        private NumberType()    {}

        @Override
        public int size() { // TODO
            return 0;
        }   // TODO

    }

    /**
     * Symbol
     */

    public static class SymbolType extends Type {

        private SymbolType()    {}

        @Override
        public int size() { return 0; }   // TODO

    }

    /**
     * Array
     */
    public static class ArrayType extends Type  {

        private final Type content;

        private ArrayType(Type type, int size)    {   // TODO - public constructor
            this.content = type;

        }


        @Override
        public int size() { // TODO
            return 0;
        }   // TODO
    }



}
