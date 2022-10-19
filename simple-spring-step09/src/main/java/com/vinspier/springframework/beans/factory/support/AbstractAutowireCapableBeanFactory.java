package com.vinspier.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.exception.PropertyException;
import com.vinspier.springframework.beans.factory.*;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanReference;
import com.vinspier.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

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
            initializeBean(beanName,bean,beanDefinition);
        } catch (Exception e) {
            throw new BeansException("create bean instance failed for bean name: " + beanName);
        }
        // *** 注册实现了DisposableBean的实例
        registryDisposableBeanIfNecessary(beanName,bean,beanDefinition);
        // *** 注册到单例 存储缓存中
        if (beanDefinition.isSingleton()) {
            registrySingleton(beanName,bean);
        }
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
        // 执行bean级别aware通知
        invokeBeanAwareMethods(bean,beanName,beanDefinition);
        System.out.println("------------------> [" + beanName +"] bean initialize starting");
        Object wrappedBean = applyBeanPostProcessorBeforeInitialization(bean,beanName);
        // 执行bean的自定义初始化方法
        try {
            invokeBeanInitMethods(wrappedBean,beanName,beanDefinition);
        } catch (Exception e) {
            throw new BeansException("execute init method failed on which bean named " + beanName);
        }
        wrappedBean = applyBeanPostProcessorAfterInitialization(wrappedBean,beanName);
        System.out.println("------------------> [" + beanName +"] bean initialize ending");
        return wrappedBean;
    }

    /**
     * bean实例 初始化前置增强
     * */
    protected Object applyBeanPostProcessorBeforeInitialization(Object originalBean, String beanName) {
        System.out.println("------------------> [" + beanName +"] bean execute enhance method before initialization");
        Object resultBean = originalBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(resultBean,beanName);
            resultBean = null != current ? current : resultBean;
        }
        return resultBean;
    }

    /**
     * 通知继承Aware的bean 使其感知容器
     * */
    protected void invokeBeanAwareMethods(Object bean, String beanName,BeanDefinition beanDefinition) {
        if (bean instanceof Aware){
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }
    }

    /**
     * 回调bean 的自定义初始化方法
     * */
    protected void invokeBeanInitMethods(Object wrappedBean, String beanName,BeanDefinition beanDefinition) throws Exception {
        // 执行 InitializingBean 逻辑
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        // 执行 配置了init-method的逻辑
        String initMethodConfigName = beanDefinition.getInitMethodName();
        if (StringUtils.isNotEmpty(initMethodConfigName) && !InitializingBean.INITIALIZE_METHOD_NAME.equals(initMethodConfigName)) {
            Method method = wrappedBean.getClass().getMethod(initMethodConfigName);
            method.invoke(wrappedBean);
        }
    }

    /**
     * bean实例 初始化后置增强
     * */
    protected Object applyBeanPostProcessorAfterInitialization(Object originalBean, String beanName) {
        System.out.println("------------------> [" + beanName +"] bean execute enhance method after initialization");
        Object resultBean = originalBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(resultBean,beanName);
            resultBean = null != current ? current : resultBean;
        }
        return resultBean;
    }

    /**
     * 注册 DisposableBean 入口
     * 只有实现了DisposableBean接口 || 实例定义了destroy-method方法的bean才会被注册
     * */
    protected void registryDisposableBeanIfNecessary(String beanName,Object bean,BeanDefinition beanDefinition) {
        if ((bean instanceof DisposableBean) || StringUtils.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registryDisposableBean(beanName, new DisposableBeanAdapter(bean,beanName,beanDefinition.getDestroyMethodName()));
        }
    }

}
