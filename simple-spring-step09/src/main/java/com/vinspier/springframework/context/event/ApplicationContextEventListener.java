package com.vinspier.springframework.context.event;

import com.vinspier.springframework.context.ApplicationListener;

public class ApplicationContextEventListener implements ApplicationListener<ApplicationContextEvent> {

    public static final String APPLICATION_CONTEXT_EVENT_LISTENER_BEAN_NAME = "applicationContextEventListener";

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (event instanceof ApplicationContextCloseEvent) {
            close();
        }
        if (event instanceof ApplicationContextRefreshEvent) {
            refresh();
        }
    }

    public void close() {
        System.out.println("==============================>[处理 容器关闭事件通知]");
    }

    public void refresh() {
        System.out.println("==============================>[处理 容器刷新事件通知]");
    }

}
