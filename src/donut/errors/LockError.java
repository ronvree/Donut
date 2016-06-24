package donut.errors;

/**
 * Created by Gijs on 24-Jun-16.
 */
public class LockError extends Error {

    public LockError(int row, int column, String variable) {
        super(row, column);
        this.setMsg(String.format("Cannot lock on a non-global variable: %s", variable));
    }
}
