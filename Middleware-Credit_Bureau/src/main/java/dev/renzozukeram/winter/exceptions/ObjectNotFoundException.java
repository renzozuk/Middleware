package dev.renzozukeram.winter.exceptions;

import java.io.Serial;

public class ObjectNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(Class<?> clazz) {
        super(clazz.getSimpleName() + " not found.");
    }

    public ObjectNotFoundException(Class<?> clazz, String message) {
        super(clazz.getSimpleName() + " not found: " + message);
    }

    public ObjectNotFoundException(Class<?> clazz, Throwable cause) {
        super(clazz.getSimpleName() + " not found.", cause);
    }

    public ObjectNotFoundException(Class<?> clazz, String message, Throwable cause) {
        super(clazz.getSimpleName() + " not found: " + message, cause);
    }
}
