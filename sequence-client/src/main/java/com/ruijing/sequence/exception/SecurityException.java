package com.ruijing.sequence.exception;

/**
 * SecurityException
 *
 * @author mwup
 * @version 1.0
 * @created 2018/9/10 19:38
 **/
public class SecurityException extends RuntimeException {

    public SecurityException() {
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
