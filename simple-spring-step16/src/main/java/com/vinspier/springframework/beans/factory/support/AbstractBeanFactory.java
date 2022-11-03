package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.FactoryBean;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.ConfigurableBeanFactory;
import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.util.StringUtils;
import com.vinspier.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 抽象实例工厂
 * */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /**
     * bean实例级别 增强功能
     * */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * 属性值表达式处理器
     * */
    private final List<StringValueResolver> valueResolvers = new LinkedList<>();

    /**
     * 属性转换器
     * */
    private ConversionService conversionService;

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null,null);
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        return doGetBean(beanName,args,null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        // tips: 由于 容器中的bean可能为代理对象 所以不能直接转成requiredType
        // tips: requiredType 应在获取bean之前 与beanDefinition中的原始类型比较
        return doGetBean(name,null,null);
    }

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    protected abstract boolean containsBeanDefinition(String name);

    /**
     * 定义 实例创建 抽象流程
     * */
    public <T> T doGetBean(final String beanName,final Object[] args,Class<T> requiredType) {
        Object result = getSingleton(beanName);
        if (null != result){
            return getObjectForBeanInstance(result,beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
//        this.validBeanClassType(beanDefinition.getBeanClass(),requiredType);
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
        if (null != requiredType && !requiredType.isAssignableFrom(beanClazz)) {
            throw new BeansException("could not found bean required of type " + requiredType.getSimpleName());
        }
    }

    protected <T> void validBeanClassType(Object bean,Class<T> requiredType) {
        if (null != requiredType && !bean.getClass().isAssignableFrom(requiredType)) {
            throw new BeansException("could not found bean required of type " + requiredType.getSimpleName());
        }
    }

    protected <T> boolean validClassTypeAssignable(Class<?> beanClazz,Class<T> requiredType) {
        return null != requiredType && requiredType.isAssignableFrom(beanClazz);
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

    @Override
    public void addEmbeddedValueResolver(StringValueResolver resolver) {
        valueResolvers.add(resolver);
    }

    @Override
    public String resolveEmbeddedValue(String valueExpression) {
        String result;
        for (StringValueResolver resolver : valueResolvers) {
            result = resolver.resolve(valueExpression);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        }
        return null;
    }

    @Override
    public void setConvertService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public ConversionService getConvertService() {
        return this.conversionService;
    }
}
