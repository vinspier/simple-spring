package com.vinspier.springframework.aop;

/**
 * 目标被代理对象包装
 *
 * @author  xiaobiao.fan
 * @date    2022/10/20 5:58 下午
*/
public class TargetSource {

    private final Object target;

    public TargetSource(Object target) {
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return this.target.getClass();
    }

    public Class<?>[] getTargetInterfaces() {
        return this.target.getClass().getInterfaces();
    }

    public Object getTarget() {
        return target;
    }

}
