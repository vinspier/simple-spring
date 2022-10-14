package com.vinspier.springframework.beans.factory;


import com.vinspier.biz.service.StockService;
import com.vinspier.biz.service.impl.StockServiceImpl;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class BeanFactoryTest {

    @Test
    public void testFactory() {
        // 1、初始化容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2、注册bean定义
        BeanDefinition beanDefinition = new BeanDefinition(StockServiceImpl.class);
        beanFactory.registryBeanDefinition("stockService",beanDefinition);
        // 3、从容器中获取实例 会经历实例第一次初始化
        StockService stockService = (StockService) beanFactory.getBean("stockService");

        // 3、再次从容器中获取实例
        StockService stockService2 = (StockService) beanFactory.getSingleton("stockService");

        // 因为是单例 结果 true
        System.out.println(stockService == stockService2);
    }

}