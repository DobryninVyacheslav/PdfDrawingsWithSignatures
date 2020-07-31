package ru.ruselprom.signs.exceptions;

public class SignaturesAppRuntimeException extends RuntimeException {
    public SignaturesAppRuntimeException() {
    }

    public SignaturesAppRuntimeException(String message) {
        super(message);
    }

    public SignaturesAppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
