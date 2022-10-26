package com.vinspier.springframework.context.event;

import com.vinspier.springframework.beans.factory.BeanFactory;
import com.vinspier.springframework.context.ApplicationEvent;
import com.vinspier.springframework.context.ApplicationListener;

import java.util.Collection;

/**
 * 简易 事件处理器
 * */
public class SimpleApplicationEventMultiCaster extends AbstractApplicationEventMultiCater {

    public SimpleApplicationEventMultiCaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    /**
     * 事件通知监听者
     * */
    @Override
    public <E extends ApplicationEvent> void multiCastEvent(E event) {
        Collection<ApplicationListener<E>> supportListeners = super.getApplicationListeners(event);
        for (ApplicationListener<E> listener : supportListeners) {
            listener.onApplicationEvent(event);
        }
    }

}
