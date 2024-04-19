package com.toandfrompdf.exceptions;

public class IncorrectFileFormat extends Exception{
    public IncorrectFileFormat() {
    }

    public IncorrectFileFormat(String message) {
        super(message);
    }

    public IncorrectFileFormat(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFileFormat(Throwable cause) {
        super(cause);
    }

    public IncorrectFileFormat(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
