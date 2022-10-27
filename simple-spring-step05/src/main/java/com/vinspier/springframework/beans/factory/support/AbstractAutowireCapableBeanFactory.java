package com.vinspier.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.exception.PropertyException;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

/**
 * 抽象 实例创建、注册 工厂
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition,Object[] args) {
        Object bean;
        try {
            // *** 1、创建实例
            bean = createBeanInstance(beanName,beanDefinition,args);
            // *** 2、填充属性
            applyBeanPropertyValues(beanName,bean,beanDefinition);
            // *** 3、执行实例bean的初始化 和 自定义增加方法
            bean = initializeBean(beanName,bean,beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BeansException("create bean instance failed for bean name: " + beanName);
        }
        // *** 注册到单例 存储缓存中
        registrySingleton(beanName,bean);
        return bean;
    }

    /**
     * 创建bean实例
     * 实例化可以采用不同策略
     * */
    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition,Object[] args) {
        Class<?> clazz = beanDefinition.getBeanClass();
        Constructor<?> constructor = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        // 根据参数 匹配 合适的构造方法
        if (null != args) {
            for (Constructor<?> c : constructors) {
                if (c.getParameterTypes().length == args.length) {
                    constructor = c;
                    break;
                }
            }
        }
        return instantiationStrategy.instantiate(beanDefinition,beanName,constructor,args);
    }

    /**
     * 填充bean实例属性
     * todo 存在 循环依赖（后续解决）
     * */
    protected void applyBeanPropertyValues(String beanName,Object bean,BeanDefinition beanDefinition) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        // 逐个解析
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            // 属性为对象引用
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getBeanName());
            }
            if (null == value) {
                throw new PropertyException("no property value found for property name: " + name + " ,in bean: " + beanName);
            }
            // 设置属性 使用反射机制设置属性值
            BeanUtil.setFieldValue(bean,name,value);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * bean初始化动作
     * */
    protected Object initializeBean(String beanName,Object bean,BeanDefinition beanDefinition) {
        System.out.println("---------> [" + beanName +"] bean initialize starting");
        Object wrappedBean = applyBeanPostProcessorBeforeInitialization(bean,beanName);
        // todo 执行bean的自定义初始化方法
        invokeBeanInitMethods(wrappedBean,beanName,beanDefinition);
        wrappedBean = applyBeanPostProcessorAfterInitialization(wrappedBean,beanName);
        System.out.println("---------> [" + beanName +"] bean initialize ending");
        return wrappedBean;
    }

    /**
     * bean实例 初始化前置增强
     * */
    protected Object applyBeanPostProcessorBeforeInitialization(Object originalBean, String beanName) {
        System.out.println("---------> [" + beanName +"] bean execute enhance method before initialization");
        Object resultBean = originalBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(resultBean,beanName);
            resultBean = null != current ? current : resultBean;
        }
        return resultBean;
    }

    /**
     * 回调bean 的自定义初始化方法 todo
     * */
    protected void invokeBeanInitMethods(Object wrappedBean, String beanName,BeanDefinition beanDefinition) {

    }

    /**
     * bean实例 初始化后置增强
     * */
    protected Object applyBeanPostProcessorAfterInitialization(Object originalBean, String beanName) {
        System.out.println("---------> [" + beanName +"] bean execute enhance method after initialization");
        Object resultBean = originalBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(resultBean,beanName);
            resultBean = null != current ? current : resultBean;
        }
        return resultBean;
    }

}
