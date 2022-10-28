package com.vinspier.springframework.context.event;

import com.vinspier.springframework.context.ApplicationEvent;
import com.vinspier.springframework.context.ApplicationListener;

import java.util.Collection;

/**
 * 事件 集中处理中心
 * 接收到事件发布后，通知事件观察者
 * */
public interface ApplicationEventMultiCater {

    /**
     * 注册 监听器
     * */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 注册 监听器
     * */
    void addApplicationListeners(Collection<ApplicationListener<?>> listeners);

    /**
     * 移除 监听器
     * */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 处理事件 通知所有监听该事件 监听器
     * */
    <E extends ApplicationEvent> void multiCastEvent(E event);
}
