package com.vinspier.springframework.context.support;

import com.vinspier.biz.service.SpuService;
import com.vinspier.biz.service.StockService;
import com.vinspier.biz.service.impl.SpuServiceImpl;
import com.vinspier.biz.service.impl.StockServiceImpl;
import org.junit.Test;

public class ClasspathApplicationContextTest {

    @Test
    public void testScan() {
        String configLocation = "classpath:spring-scan.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        SpuService spuService = applicationContext.getBean("spuService", SpuServiceImpl.class);
        System.out.println(spuService.getSpuName());
    }

    @Test
    public void testAnnotation() {
        String configLocation = "classpath:spring-scan-annotation.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        StockService stockService = applicationContext.getBean("stockServiceImpl", StockServiceImpl.class);
        System.out.println(stockService.getWarehouse());
    }


    @Test
    public void testCircleDependency() {
        String configLocation = "classpath:spring-scan-annotation.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        StockService stockService = applicationContext.getBean("stockServiceImpl", StockServiceImpl.class);
        SpuService spuService = applicationContext.getBean("spuService", SpuServiceImpl.class);
        stockService.printSpuService();
        spuService.printStockService();
    }

}