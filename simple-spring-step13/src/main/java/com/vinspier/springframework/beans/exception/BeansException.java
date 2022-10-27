package com.vinspier.springframework.beans.exception;

/**
 * 实例异常
 * */
public class BeansException extends RuntimeException {

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }

}
