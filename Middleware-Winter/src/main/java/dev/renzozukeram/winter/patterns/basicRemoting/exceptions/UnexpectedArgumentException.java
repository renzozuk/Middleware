package dev.renzozukeram.winter.patterns.basicRemoting.exceptions;

public class UnexpectedArgumentException extends RemotingError {

    public UnexpectedArgumentException(String expectedTypeArgument, String typedArgument) {
        super("The type of argument that was expected is " + expectedTypeArgument + ", but you typed '" + typedArgument + "'.");
    }

    public UnexpectedArgumentException(String expectedTypeArgument, String typedArgument, Throwable cause) {
        super("The type of argument that was expected is " + expectedTypeArgument + ", but you typed '" + typedArgument + "'.", cause);
    }
}
