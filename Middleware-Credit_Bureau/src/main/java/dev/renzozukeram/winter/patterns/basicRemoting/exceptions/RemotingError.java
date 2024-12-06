package dev.renzozukeram.winter.patterns.basicRemoting.exceptions;

public class RemotingError extends RuntimeException {

    public RemotingError(String message) {
        super(message);
    }

    public RemotingError(String message, Throwable cause) {
        super(message, cause);
    }
}
