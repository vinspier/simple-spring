package com.vinspier.biz.dao;

import com.vinspier.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 代理bean
 * */
public class StockDaoProxyBeanFactory implements FactoryBean<StockDao> {
    @Override
    public StockDao getObject() {
        InvocationHandler handler = (proxy, method, args) -> {
            if ("toString".equals(method.getName())) {
                return this.toString();
            }
            if ("getStock".equals(method.getName())) {
                Map<Long,Long> skuStockMap = new HashMap<>();
                skuStockMap.put(1110L,100L);
                skuStockMap.put(1111L,200L);
                skuStockMap.put(1112L,300L);
                System.out.println("------------------> 方法：" + method.getName() + " 被代理执行了");
                return skuStockMap.get(Long.parseLong(args[0].toString()));
            }
            return new Object();
        };
        return (StockDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[]{StockDao.class},handler);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return StockDao.class;
    }
}
