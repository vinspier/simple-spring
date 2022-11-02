package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.DisposableBean;
import com.vinspier.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * DisposableBean适配器
 *
 * 未实现DisposableBean接口的bean 但是配置了destroyMethod方法的
 * 需要适配成DisposableBean
 * */
public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final String beanName;

    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, String destroyMethodName) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = destroyMethodName;
    }

    /**
     * 包装销毁逻辑
     * */
    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        // jdk反射 完成自定义配置销毁
        if (StringUtils.isNotEmpty(destroyMethodName) && !DESTROY_METHOD_NAME.equals(destroyMethodName)) {
            Method method = bean.getClass().getMethod(destroyMethodName);
            method.invoke(bean);
        }
    }

}
