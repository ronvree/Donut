package donut.errors;

/**
 * Created by Ron on 19-6-2016.
 */
public class SyntaxError extends Error {

    public SyntaxError(int row, int column) {
        super(row, column);
        this.setMsg("Syntax Error");
    }


}
