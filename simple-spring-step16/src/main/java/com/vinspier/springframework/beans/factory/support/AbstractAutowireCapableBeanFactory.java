package com.vinspier.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.TypeUtil;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.annotation.AutowiredAnnotationBeanPostProcessor;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.exception.PropertyException;
import com.vinspier.springframework.beans.factory.*;
import com.vinspier.springframework.beans.factory.config.BeanPostProcessor;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanReference;
import com.vinspier.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.vinspier.springframework.core.convert.ConversionService;
import com.vinspier.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 抽象 实例创建、注册 工厂
 * */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new JdkInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition,Object[] args) {
        // *** 判断 bean创建是否由外部指定创建
        Object bean = resolveBeforeBeanInstantiation(beanName,beanDefinition);
        if (null != bean) {
            return bean;
        }
        return doCreateBean(beanName,beanDefinition,args);
    }

    /**
     * spring内部创建bean实例流程
     * */
    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition,Object[] args) {
        Object bean;
        try {
            // *** 1、创建实例
            bean = createBeanInstance(beanName,beanDefinition,args);
            // *** 1.1 处理循环依赖
            if (beanDefinition.isSingleton()) {
                Object exposedBean = bean;
                registrySingletonFactory(beanName,() -> getEarlyBeanReference(beanName,beanDefinition,exposedBean));
            }
            // *** 1.3 bean实例创建后 执行实例化后置增强
            boolean continuePropertyPopulation = applyBeanPostProcessorAfterInstantiation(beanName,bean,beanDefinition);
            if (!continuePropertyPopulation) {
                return bean;
            }
            // *** 1.5 在设置beanDefinition的属性前 允许beanPostProcessor修改属性
            // 主要用于一些特定属性，已经 内部的注解式属性注入
            applyBeanPostProcessorBeforeApplyingBeanPropertyValues(beanName,bean,beanDefinition);
            // *** 2、填充属性
            applyBeanPropertyValues(beanName,bean,beanDefinition);
            // *** 3、执行实例bean的创建通知、初始化 、 自定义增加的初始化方法
            bean = initializeBean(beanName,bean,beanDefinition);
        } catch (Exception e) {
            throw new BeansException("create bean instance failed for bean named: " + beanName);
        }
        // *** 注册实现了DisposableBean的实例
        registryDisposableBeanIfNecessary(beanName,bean,beanDefinition);
        Object determinedBean = bean;
        // *** 注册到单例 存储缓存中
        if (beanDefinition.isSingleton()) {
            // 从beanFactory多级缓存中获取最终的bean实例（可能为代理对象）
            determinedBean = getSingleton(beanName);
            registrySingleton(beanName,determinedBean);
        }
        return determinedBean;
    }

    /**
     * 处理实例化之前 是否需要代理工厂创建
     * */
    protected Object resolveBeforeBeanInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorBeforeInstantiation(beanDefinition.getBeanClass(),beanName);
        if (bean != null) {
            bean = applyBeanPostProcessorAfterInitialization(bean,beanName);
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
     * 指定最终的实例创建
     * 结果：无需代理 返回 originBean
     * 需代理 返回 代理对象
     * */
    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition,Object originBean) {
        Object exposedBean = originBean;
        List<BeanPostProcessor> processors = getBeanPostProcessors();
        if (CollectionUtil.isEmpty(processors)) {
            return exposedBean;
        }
        for (BeanPostProcessor processor : processors) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                // *** bean实例化 后置增强
                exposedBean = ((InstantiationAwareBeanPostProcessor)processor).getEarlyBeanReference(beanName,exposedBean);
                if (null != exposedBean) {
                    return exposedBean;
                }
            }
        }
        return exposedBean;
    }

    /**
     * 结果 返回false 拦截后续属性填充操作 直接返回当前bean
     * */
    protected boolean applyBeanPostProcessorAfterInstantiation(String beanName,Object bean,BeanDefinition beanDefinition) {
        List<BeanPostProcessor> processors = getBeanPostProcessors();
        if (CollectionUtil.isEmpty(processors)) {
            return true;
        }
        for (BeanPostProcessor processor : processors) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                // *** bean实例化 后置增强
                boolean continuePropertyPopulation = ((InstantiationAwareBeanPostProcessor)processor).postProcessAfterInstantiation(bean,beanName);
                if (!continuePropertyPopulation) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 注解式 属性注入回调
     * */
    protected void applyBeanPostProcessorBeforeApplyingBeanPropertyValues(String beanName,Object bean,BeanDefinition beanDefinition) {
        List<BeanPostProcessor> processors = getBeanPostProcessors();
        if (CollectionUtil.isEmpty(processors)) {
            return;
        }
        for (BeanPostProcessor processor : processors) {
            if (processor instanceof AutowiredAnnotationBeanPostProcessor) {
                // *** 注解式 属性注入回调
                PropertyValues pvs = ((AutowiredAnnotationBeanPostProcessor)processor).postProcessPropertyValues(beanDefinition.getPropertyValues(),bean,beanName);
                if (null != pvs) {
                    for (PropertyValue pv : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(pv);
                    }
                }
            }
        }
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
            } else {
                // 属性类型转换处理
                ConversionService conversionService = super.getConvertService();
                if (null != conversionService) {
                   Class<?> sourceType = value.getClass();
                   Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(),name);
                   if (conversionService.canConvert(sourceType,targetType)) {
                       value = conversionService.convert(value,targetType);
                   }
                }
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
     * bean实例化前置增强
     * */
    protected Object applyBeanPostProcessorBeforeInstantiation(Class<?> clazz, String beanName) {
        System.out.println("------------------> judge [" + clazz.getSimpleName() +"] class whether matches [aop pointcut definition] before bean Instantiation!");
        Object resultBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                resultBean = ((InstantiationAwareBeanPostProcessor) processor).postProcessBeforeInstantiation(clazz,beanName);
                if (null != resultBean) {
                    return resultBean;
                }
            }
        }
        return null;
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
            // *** 动态代理对象 在所有属性填充到bean中去后 在DefaultAdvisorAutoProxyCreator中创建
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
