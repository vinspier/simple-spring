package com.vinspier.springframework.context.event;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.BeanFactory;
import com.vinspier.springframework.beans.factory.BeanFactoryAware;
import com.vinspier.springframework.context.ApplicationEvent;
import com.vinspier.springframework.context.ApplicationListener;
import com.vinspier.springframework.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 抽象 事件处理中心
 * */
public abstract class AbstractApplicationEventMultiCater implements ApplicationEventMultiCater, BeanFactoryAware {

    // 监听器 容器
    private final Set<ApplicationListener<? extends ApplicationEvent>> listeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        this.listeners.add(listener);
    }

    @Override
    public void addApplicationListeners(Collection<ApplicationListener<?>> listeners) {
        this.listeners.addAll(listeners);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        this.listeners.remove(listener);
    }

    @Override
    public <E extends ApplicationEvent> void multiCastEvent(E event) {
        throw new UnsupportedOperationException("event multi caste method must be overwrite in it's  implemented by class!");
    }

    @SuppressWarnings("unchecked")
    protected <E extends ApplicationEvent> Collection<ApplicationListener<E>> getApplicationListeners(E event) {
        List<ApplicationListener<E>> supportListeners = new LinkedList<>();
        for (ApplicationListener listener : listeners) {
            if (supportEvent(listener,event)) {
                supportListeners.add(listener);
            }
        }
        return supportListeners;
    }

    /**
     * 判断监听器 是否对改事件 感兴趣
     * 即 : 泛型E 是否是event的子类
     * */
    protected boolean supportEvent(ApplicationListener<?> listener,ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClazz = listener.getClass();
        Class<?> targetClazz = ClassUtils.isCglibProxyClass(listenerClazz) ? listenerClazz.getSuperclass() : listenerClazz;
        Type genericInterface = targetClazz.getGenericInterfaces()[0];
        // 接口声明参数类型
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String typeArgumentClassName = actualTypeArgument.getTypeName();
        Class<?> eventDeclareClazz;
        try {
            eventDeclareClazz = Class.forName(typeArgumentClassName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("not found event class for named : " + typeArgumentClassName);
        }
        return eventDeclareClazz.isAssignableFrom(event.getClass());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
