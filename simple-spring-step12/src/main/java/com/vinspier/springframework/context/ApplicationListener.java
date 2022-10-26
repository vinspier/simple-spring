package com.vinspier.springframework.context;

import java.util.EventListener;

/**
 * 事件 监听器（观察者）
 * */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 监听事件 处理
     * */
    void onApplicationEvent(E event);

}
