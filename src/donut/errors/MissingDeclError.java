package donut.errors;

/**
 * Created by Ron on 19-6-2016.
 */
public class MissingDeclError extends Error {

    public MissingDeclError(int row, int column, String id) {
        super(row, column);
        this.setMsg(String.format("Missing declaration of variable %s", id));
    }
}
