package com.vinspier.springframework.beans.factory.support;

import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.DisposableBean;
import com.vinspier.springframework.beans.factory.ObjectFactory;
import com.vinspier.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 单例行为抽象
 * */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * 一级缓存
     * 最总完成bean实例
     *
     * 最终向外界提供的bean实例
     * tips: 最后的完成实例bean
     * */
    private final Map<String,Object> singletonBeansMap = new HashMap<>();

    /**
     * 二级缓存
     * 提前暴露 供其他bean感知 （刚创建的bean 暂未被引用过的bean）
     *
     * tips: 暂未被引用过的bean，由三级缓存晋升而来，
     * 被引用过后 在下一次注册时晋升至一级缓存
     * */
    private final Map<String,Object> earlySingletonBeansMap = new HashMap<>();

    /**
     * 三级缓存
     * ObjectFactory<?> 定义bean创建来源的行为
     *
     * tips: 从未创建过的bean来源途径(代理对象、普通对象)
     * */
    private final Map<String,ObjectFactory<?>> singletonFactoriesMap = new HashMap<>();

    /**
     * 所有继承了DisposableBean接口的实例
     * */
    private final Map<String, DisposableBean> disposableBeansMap = new HashMap<>();

    @Override
    public void registrySingleton(String beanName, Object bean) {
        singletonBeansMap.put(beanName,bean);
        earlySingletonBeansMap.remove(beanName);
        singletonFactoriesMap.remove(beanName);
    }

    protected void registrySingletonFactory(String beanName, ObjectFactory<?> factory) {
        if (singletonBeansMap.containsKey(beanName)) {
            return;
        }
        singletonFactoriesMap.put(beanName,factory);
        earlySingletonBeansMap.remove(beanName);
    }

    /**
     * 从一级到三级缓存逐一寻找
     * */
    @Override
    public Object getSingleton(String beanName) {
        Object result = singletonBeansMap.get(beanName);
        if (null == result) {
            result = earlySingletonBeansMap.get(beanName);
            // 从三级创建源头获取
            if (null == result) {
                ObjectFactory<?> of = singletonFactoriesMap.get(beanName);
                if (null != of) {
                    result = of.getObject();
                }
            }
        }
        return result;
    }

    public void registryDisposableBean(String beanName,DisposableBean disposableBean) {
        this.disposableBeansMap.put(beanName,disposableBean);
    }

    /**
     * 销毁实例 具体实现
     * */
    public void destroySingletons() {
        Set<String> disposableBeanNames = disposableBeansMap.keySet();
        for (String name : disposableBeanNames) {
            try {
                disposableBeansMap.get(name).destroy();
                disposableBeansMap.remove(name);
            } catch (Exception e) {
                throw new BeansException("execute destroy method failed on bean named : " + name);
            }
        }
    }

}
