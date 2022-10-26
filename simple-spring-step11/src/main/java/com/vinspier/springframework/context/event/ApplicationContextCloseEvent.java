package com.vinspier.springframework.context.event;

public class ApplicationContextCloseEvent extends ApplicationContextEvent {

    public ApplicationContextCloseEvent(Object source) {
        super(source);
    }

}
