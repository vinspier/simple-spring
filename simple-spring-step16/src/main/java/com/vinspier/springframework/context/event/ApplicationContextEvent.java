package com.vinspier.springframework.context.event;

import com.vinspier.springframework.context.ApplicationEvent;

/**
 * 上下文容器 自身事件
 * */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    public ApplicationContextEvent(Object source) {
        super(source);
    }

}
