package com.vinspier.springframework.aop;

import com.vinspier.springframework.util.ClassUtils;

/**
 * 目标被代理对象包装
 *
 * @author  xiaobiao.fan
 * @date    2022/10/20 5:58 下午
*/
public class TargetSource {

    /**
     * 在加入aop切面支持后
     * 原始target对象都可能是代理对象
     * */
    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        Class<?> clazz = target.getClass();
        if (ClassUtils.isCglibProxyClass(clazz)) {
            return clazz.getSuperclass();
        }
        return clazz;
    }

    public Class<?>[] getTargetInterfaces() {
        Class<?> clazz = getTargetClass();
        return clazz.getInterfaces();
    }

    public Object getTarget() {
        return target;
    }

}
