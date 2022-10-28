package com.vinspier.springframework.context;

/**
 * 事件发布器
 * */
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     * */
    void publishEvent(ApplicationEvent event);

}
