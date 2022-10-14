package com.vinspier.springframework.beans.factory;


import com.vinspier.biz.dao.impl.StockDaoImpl;
import com.vinspier.biz.service.StockService;
import com.vinspier.biz.service.impl.StockServiceImpl;
import com.vinspier.springframework.beans.PropertyValue;
import com.vinspier.springframework.beans.PropertyValues;
import com.vinspier.springframework.beans.factory.config.BeanDefinition;
import com.vinspier.springframework.beans.factory.config.BeanReference;
import com.vinspier.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import com.vinspier.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.Test;

public class BeanFactoryTest {

    @Test
    public void testFactory() {
        // 1、初始化容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2、注册bean定义
        // 2、1 注册dao
        BeanDefinition daoBeanDefinition = new BeanDefinition(StockDaoImpl.class);
        beanFactory.registryBeanDefinition("stockDao",daoBeanDefinition);
        // 2、2 注册service
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("stockDao",new BeanReference("stockDao")));
        propertyValues.addPropertyValue(new PropertyValue("warehouse","ewe"));
        BeanDefinition beanDefinition = new BeanDefinition(StockServiceImpl.class,propertyValues);
        beanFactory.registryBeanDefinition("stockService",beanDefinition);
        // 3、从容器中获取实例 会经历第一次实例初始化
        StockService stockService = (StockService) beanFactory.getBean("stockService");
        System.out.println("--------> get stock result for skuId 1110 is [" + stockService.getStock(1110L) + "]");
        // 3、再次从容器中获取实例
        StockService stockService2 = (StockService) beanFactory.getSingleton("stockService");

        // 因为是单例 结果 true
        System.out.println("--------> " + (stockService == stockService2));
    }

    @Test
    public void testXmlReaderFactory() {
        // 1、初始化容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 2、创建数据源读取策略
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        String location = "classpath:spring.xml";
        xmlBeanDefinitionReader.loadBeanDefinitions(location);

        // 3、从容器中获取实例 会经历第一次实例初始化
        StockService stockService = (StockService) beanFactory.getBean("stockService");
        System.out.println("--------> get stock result for skuId 1110 is [" + stockService.getStock(1110L) + "]");
        // 3、再次从容器中获取实例
        StockService stockService2 = (StockService) beanFactory.getSingleton("stockService");

        // 因为是单例 结果 true
        System.out.println("--------> " + (stockService == stockService2));
    }

}