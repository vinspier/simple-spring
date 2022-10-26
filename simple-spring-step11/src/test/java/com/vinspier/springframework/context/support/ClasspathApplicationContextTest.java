package com.vinspier.springframework.context.support;

import com.vinspier.biz.service.StockService;
import com.vinspier.biz.service.impl.StockServiceImpl;
import org.junit.Test;

public class ClasspathApplicationContextTest {

    @Test
    public void testScan() {
        String configLocation = "classpath:spring-scan.xml";
        ClasspathApplicationContext applicationContext = new ClasspathApplicationContext(configLocation);
        StockService stockService = applicationContext.getBean("stockService", StockServiceImpl.class);
        System.out.println(stockService.getWarehouse());
    }

}