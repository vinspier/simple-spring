package com.vinspier.springframework.beans.exception;

/**
 * 实例异常
 * */
public class PropertyException extends RuntimeException {

    public PropertyException(String message) {
        super(message);
    }

    public PropertyException(String message, Throwable cause) {
        super(message, cause);
    }

}
