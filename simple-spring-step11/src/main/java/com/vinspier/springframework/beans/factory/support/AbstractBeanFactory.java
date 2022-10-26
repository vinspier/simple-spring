package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.FactoryBean;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象实例工厂
 * */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /**
     * bean实例级别 增强功能
     * */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String beanName) {
        return getBean(beanName, (Object) null);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName,args,null);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        return doGetBean(name,null,null);
    }

    /**
     * 定义 实例创建 抽象流程
     * */
    public <T> T doGetBean(final String beanName,final Object[] args,Class<T> requiredType) {
        Object result = getSingleton(beanName);
        if (null != result){
            return getObjectForBeanInstance(result,beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        this.validBeanClassType(beanDefinition.getBeanClass(),requiredType);
        result = createBean(beanName,beanDefinition,args);
        return getObjectForBeanInstance(result,beanName);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getObjectForBeanInstance(Object bean,String name) {
        if (!(bean instanceof FactoryBean)) {
            return (T) bean;
        }
        Object object = getCachedObjectFromFactoryBean(name);
        if (object == null) {
            object = getObjectFromFactoryBean((FactoryBean<?>) bean,name);
        }
        return (T) object;
    }

    protected <T> void validBeanClassType(Class<?> beanClazz,Class<T> requiredType) {
        if (null != requiredType && !beanClazz.isAssignableFrom(requiredType)) {
            throw new BeansException("could not found bean required of type " + requiredType.getSimpleName());
        }
    }

    protected <T> void validBeanClassType(Object bean,Class<T> requiredType) {
        if (null != requiredType && !bean.getClass().isAssignableFrom(requiredType)) {
            throw new BeansException("could not found bean required of type " + requiredType.getSimpleName());
        }
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName);

    protected abstract Object createBean(String beanName,BeanDefinition beanDefinition,Object[] args);

    @Override
    public void addBeanPostProcessor(BeanPostProcessor processor) {
        beanPostProcessors.remove(processor);
        beanPostProcessors.add(processor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    protected ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

}
