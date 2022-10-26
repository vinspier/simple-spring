package com.vinspier.springframework.context;

import java.util.EventObject;

/**
 * 定义 事件抽象
 * */
public abstract class ApplicationEvent extends EventObject {

    public ApplicationEvent(Object source) {
        super(source);
    }

}
