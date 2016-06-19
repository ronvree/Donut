package donut;

/**
 * Created by Ron on 19-6-2016.
 */
public abstract class Type {

    public static final ReactionType REACTION_TYPE = new ReactionType();
    public static final NumberType NUMBER_TYPE = new NumberType();

    public abstract int size();

    public static class ReactionType extends Type   {

        private ReactionType()  {}

        @Override
        public int size() { // TODO
            return 0;
        }

    }

    public static class NumberType extends Type {

        private NumberType()    {}

        @Override
        public int size() { // TODO
            return 0;
        }

    }

    public static class ArrayType extends Type  {

        private final Type content;

        private ArrayType(Type type)    {
            this.content = type;

        }


        @Override
        public int size() { // TODO
            return 0;
        }
    }



}
