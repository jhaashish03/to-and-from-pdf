package com.toandfrompdf.exceptions;

public class ImageFormatChangeException extends Exception{
    public ImageFormatChangeException() {
    }

    public ImageFormatChangeException(String message) {
        super(message);
    }

    public ImageFormatChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageFormatChangeException(Throwable cause) {
        super(cause);
    }

    public ImageFormatChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
