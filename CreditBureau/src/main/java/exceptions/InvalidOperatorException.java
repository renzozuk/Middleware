package exceptions;

public class InvalidOperatorException extends RuntimeException {

    public InvalidOperatorException(String operator) {
        super("The operator '" + operator + "' is not valid.");
    }
}
