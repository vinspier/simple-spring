package com.vinspier.springframework.beans.annotation;

import cn.hutool.core.bean.BeanUtil;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.BeanFactory;
import com.vinspier.springframework.beans.factory.BeanFactoryAware;
import com.vinspier.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.vinspier.springframework.beans.factory.support.ConfigurableListableBeanFactory;
import com.vinspier.springframework.util.ClassUtils;

import java.lang.reflect.Field;

/**
 * 注解式属性值 内置解析器
 *
 * @author  xiaobiao.fan
 * @date    2022/10/26 2:57 下午
*/
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        // 1、处理@Value属性配置值
        Class<?> clazz = bean.getClass();
        if (ClassUtils.isCglibProxyClass(clazz)) {
            clazz = clazz.getSuperclass();
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        // 2、处理bean注册式注入
        resolveValueAnnotation(declaredFields,bean);
        resolveAutowiredAnnotation(declaredFields,bean);
        return pvs;
    }

    private void resolveValueAnnotation(Field[] declaredFields,Object bean) {
        if (null == declaredFields || declaredFields.length < 1) {
            return;
        }
        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (null != valueAnnotation) {
                String valueExpression = valueAnnotation.value();
                String value = this.beanFactory.resolveEmbeddedValue(valueExpression);
                BeanUtil.setFieldValue(bean,field.getName(),value);
            }
        }
    }

    private void resolveAutowiredAnnotation(Field[] declaredFields,Object bean) {
        if (null == declaredFields || declaredFields.length < 1) {
            return;
        }
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (null != autowiredAnnotation) {
                // *** 说明了Autowired注解
                Class<?> filedType = field.getType();
                Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
                Object fetchedBean = null;
                if (null == qualifierAnnotation) {
                    fetchedBean = beanFactory.getBean(filedType);
                } else {
                    String qualifierBeanName = qualifierAnnotation.value();
                    fetchedBean = beanFactory.getBean(qualifierBeanName,filedType);
                }
                if (null == fetchedBean) {
                    throw new BeansException(String.format("could not find a bean of %s type by autowired annotation",filedType.getSimpleName()));
                }
                BeanUtil.setFieldValue(bean,field.getName(),fetchedBean);
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }

}
