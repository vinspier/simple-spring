package com.vinspier.springframework.context.support;

import com.vinspier.springframework.beans.factory.config.BeanFactoryPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import com.vinspier.springframework.context.ApplicationEvent;
import com.vinspier.springframework.context.ApplicationListener;
import com.vinspier.springframework.context.ConfigurableApplicationContext;
import com.vinspier.springframework.context.event.*;
import com.vinspier.springframework.core.io.AbstractResourceLoader;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 上下文 抽象行为定义
 * */
public abstract class AbstractApplicationContext extends AbstractResourceLoader implements ConfigurableApplicationContext {

    private final String APPLICATION_EVENT_MULTI_CATER_BEAN_NAME = "applicationEventMultiCater";

    private ApplicationEventMultiCater applicationEventMultiCater;

    /**
     * 定义刷新容器 模版方法
     * 具体行为 由实现者完成
     * */
    @Override
    public void refresh() {
        // 1、刷新 创建新容器
        refreshBeanFactory();
        // 2、获取容器
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 3、***** 注册 上下文容器通知处理器 使得继承ApplicationContextAware的bean感知其所属的bean容器
        getBeanFactory().addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 4、容器级别 bean实例化前置处理
        invokeBeanFactoryPostProcessors(beanFactory);
        // 5、注册 实例级别 增强处理 tips: *** 提前于其他bean
        registryBeanPostProcessors(beanFactory);
        // 6、初始化 事件处理中心
        initApplicationEventMultiCater();
        // 7、注册 事件监听器
        registerApplicationListeners();
        // 8、提前实例化bean单例
        beanFactory.preInstantiateSingletons();
        // 9、发布容器刷新成功
        refreshFinished();
    }

    /**
     * 刷新 创建新容器
     * */
    protected abstract void refreshBeanFactory();

    /**
     * 获取容器
     * */
    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        System.out.println("---------> beanFactory加载元数据完成 进入用户自定义增加方法");
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beanFactoryPostProcessorMap.forEach((beanName,processor) -> {
            processor.postProcessBeanFactory(beanFactory);
        });
    }

    protected void registryBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beanPostProcessorMap.forEach((beanName,processor) -> {
            System.out.println("[" + processor.getClass().getSimpleName() + "]" + "bean processor has been registry.");
            beanFactory.addBeanPostProcessor(processor);
        });
    }

    /**
     * 初始化 事件处理中心
     * */
    protected void initApplicationEventMultiCater() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        this.applicationEventMultiCater = new SimpleApplicationEventMultiCaster(beanFactory);
        beanFactory.registrySingleton(APPLICATION_EVENT_MULTI_CATER_BEAN_NAME,applicationEventMultiCater);
    }

    protected void registerApplicationListeners() {
        // 注册application自身事件监听
        ApplicationContextEventListener applicationContextEventListener = new ApplicationContextEventListener();
        this.applicationEventMultiCater.addApplicationListener(applicationContextEventListener);
        // 注册 外部程序定义监听器
        getBeanFactory().registrySingleton(ApplicationContextEventListener.APPLICATION_CONTEXT_EVENT_LISTENER_BEAN_NAME,applicationContextEventListener);
        Collection<ApplicationListener> listeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : listeners) {
            this.applicationEventMultiCater.addApplicationListener(listener);
        }
    }

    protected void refreshFinished() {
        this.publishRefreshFinishedEvent();
    }

    protected void publishRefreshFinishedEvent() {
        System.out.println("==============================>[发布 容器刷新事件通知]");
        this.applicationEventMultiCater.multiCastEvent(new ApplicationContextRefreshEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.applicationEventMultiCater.multiCastEvent(event);
    }

    @Override
    public Object getBean(String beanName) {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return getBeanFactory().getBean(beanName,args);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        return getBeanFactory().getBean(name,type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return getBeanFactory().getBeansOfType(clazz);
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return getBeanFactory().getBean(type);
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void close() {
        this.publishApplicationClosedEvent();
        getBeanFactory().destroySingletons();
    }

    protected void publishApplicationClosedEvent() {
        System.out.println("==============================>[发布 容器关闭事件通知]");
        this.applicationEventMultiCater.multiCastEvent(new ApplicationContextCloseEvent(this));
    }
    @Override
    public void registryShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

}
