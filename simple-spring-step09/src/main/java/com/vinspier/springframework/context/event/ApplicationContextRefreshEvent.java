package com.vinspier.springframework.context.event;
/**
 * 上下文容器 刷新事件
 * */
public class ApplicationContextRefreshEvent extends ApplicationContextEvent {

    public ApplicationContextRefreshEvent(Object source) {
        super(source);
    }

}
