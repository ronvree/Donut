package donut.errors;

/**
 * Created by Ron on 20-6-2016.
 */
public class DoubleDeclError extends Error {

    public DoubleDeclError(int row, int column, String id) {
        super(row, column);
        this.setMsg(String.format("Variable %s already declared!", id));
    }
}
