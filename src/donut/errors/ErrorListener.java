package donut.errors;

import java.util.ArrayList;
import java.util.List;
import donut.errors.Error;

import donut.errors.SyntaxError;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/** Antlr error listener to collect errors rather than send them to stderr. */
public class ErrorListener extends BaseErrorListener {
    /** Errors collected by the listener. */
    private final List<Error> errors;

    public ErrorListener() {
        this.errors = new ArrayList<>();
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol, int line, int charPositionInLine,
                            String msg, RecognitionException e) {
        this.errors.add(new SyntaxError(line, charPositionInLine, msg));
    }

    /** Indicates if the listener has collected any errors. */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /** Returns the (possibly empty) list of errors collected by the listener. */
    public List<Error> getErrors() {
        return this.errors;
    }
}
