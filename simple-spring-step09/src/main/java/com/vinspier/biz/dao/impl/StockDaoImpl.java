package com.vinspier.biz.dao.impl;

import com.vinspier.biz.dao.StockDao;
import com.vinspier.springframework.beans.factory.DisposableBean;
import com.vinspier.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

public class StockDaoImpl implements StockDao, InitializingBean, DisposableBean {

    /**
     * simulate sku stock database
     * 模拟db库存
     * */
    private final static Map<Long,Long> skuStockMap = new HashMap<>();

    static {
        skuStockMap.put(1110L,100L);
        skuStockMap.put(1111L,200L);
        skuStockMap.put(1112L,300L);
    }

    @Override
    public Long getStock(Long skuId) {
        return skuStockMap.get(skuId);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("-------------------------> 执行实现InitializingBean的初始化方法afterPropertiesSet");
    }

    public void customizeInitMethod() {
        System.out.println("-------------------------> 执行实例本身配置的初始化方法[customizeInitMethod]");
        skuStockMap.put(1110L,10000L);
        skuStockMap.put(1111L,20000L);
        skuStockMap.put(1112L,30000L);
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("-------> 执行实现DisposableBean的销毁方法destroy");
    }

    public void customizeDestroyMethod() {
        System.out.println("-------> 执行实例本身配置的销毁方法[customizeDestroyMethod]");
    }

}
