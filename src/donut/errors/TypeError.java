package donut.errors;

import donut.Type;

/**
 * Created by Ron on 19-6-2016.
 */
public class TypeError extends Error {

    public TypeError(int row, int column, Type expected, Type found) {
        super(row, column);
        this.setMsg(String.format("Type Error: expected %s, found %s", expected, found));
    }

}
