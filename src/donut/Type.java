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

    public abstract TypeKind getTypeKind();

    @Override
    public boolean equals(Object type) {
        if (this instanceof ArrayType)   {
            if (type instanceof ArrayType)   {
                return ((ArrayType) this).content.equals(((ArrayType) type).content);
            } else {
                return false;
            }
        } else {
            return this == type;
        }

    }

    @Override
    public String toString()    {
        return getTypeKind().toString();
    }

    /**
     * Reaction
     */
    public static class ReactionType extends Type   {

        private ReactionType()  {}

        @Override
        public int size() { // TODO
            return 1;
        }

        @Override
        public TypeKind getTypeKind()   { return TypeKind.REACTION; }

    }

    /**
     * Number
     */
    public static class NumberType extends Type {

        private NumberType()    {}

        @Override
        public int size() { // TODO
            return 1;
        }

        @Override
        public TypeKind getTypeKind()   { return TypeKind.NUMBER; }

    }

    /**
     * Symbol
     */

    public static class SymbolType extends Type {

        private SymbolType()    {}

        @Override
        public int size() { return 1; }   // TODO

        @Override
        public TypeKind getTypeKind()   { return TypeKind.SYMBOL; }

    }

    /**
     * Array
     */
    public static class ArrayType extends Type  {

        private final Type content;

        public ArrayType(Type type)    {   // TODO - public constructor
            this.content = type;

        }

        @Override
        public int size() { // TODO
            return 0;
        }   // TODO

        @Override
        public TypeKind getTypeKind()   { return TypeKind.ARRAY; }

        @Override
        public String toString()    {
            return super.toString() + "-" + content;
        }

    }

}
