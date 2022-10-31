package com.vinspier.springframework.beans.factory;

import com.vinspier.springframework.beans.exception.BeansException;

/**
 * bean实例创建工厂(用作三级缓存bean来源 主要作为代理对象创建入口)
 * 定义bean的创建途径行为 可来自jdk反射创建 或 代理工厂创建
 *
 * @author  xiaobiao.fan
 * @date    2022/10/29 5:14 下午
*/
@FunctionalInterface
public interface ObjectFactory<T> {

    /**
     * (符合切面定义、需事物管理等)
     * 若需要生成代理对象 返回代理对象
     * */
    T getObject() throws BeansException;

}
