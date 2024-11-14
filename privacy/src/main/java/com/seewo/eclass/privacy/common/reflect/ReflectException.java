package com.seewo.eclass.privacy.common.reflect;

/**
 * Copy from https://github.com/jOOQ/jOOR
 */
public class ReflectException extends RuntimeException {

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException() {
        super();
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}