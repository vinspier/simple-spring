package com.vinspier.biz.service.impl;

import com.vinspier.biz.dao.StockDao;
import com.vinspier.biz.service.StockService;
import com.vinspier.springframework.beans.exception.BeansException;
import com.vinspier.springframework.beans.factory.BeanClassLoaderAware;
import com.vinspier.springframework.beans.factory.BeanFactory;
import com.vinspier.springframework.beans.factory.BeanFactoryAware;
import com.vinspier.springframework.beans.factory.BeanNameAware;
import com.vinspier.springframework.context.ApplicationContext;
import com.vinspier.springframework.context.ApplicationContextAware;

public class StockServiceImpl implements StockService, BeanFactoryAware, BeanClassLoaderAware, BeanNameAware, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    private StockDao stockDao;

    private String warehouse;

    @Override
    public Long getStock(Long skuId) {
        return stockDao.getStock(skuId);
    }

    @Override
    public String getWarehouse() {
        return this.warehouse;
    }

    @Override
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public StockDao getStockDao() {
        return stockDao;
    }

    public void setStockDao(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("------------------------> [执行bean的ClassLoader回调通知]");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("------------------------> [执行beanFactory回调通知]");
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("------------------------> [执行bean的名称回调通知]");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("------------------------> [执行applicationContext回调通知]");
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
