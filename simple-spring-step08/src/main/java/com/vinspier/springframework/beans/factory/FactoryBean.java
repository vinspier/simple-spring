package com.vinspier.springframework.beans.factory;

/**
 * 工厂bean 可拓展外部程序自动注入实例至spring容器
 * bean的拓展，即可将本身注册到spring容器中
 * 也可以 将getObject的结果 注册到spring容器(FactoryBeanRegistrySupport)
 *
 * 例如：spring整合mybatis时，当mybatis加载完配置后 需要像spring容器中
 * 注入一个sqlSessionFactory实例
 * */
public interface FactoryBean<T> {

    T getObject();

    boolean isSingleton();

    Class<?> getObjectType();

}
