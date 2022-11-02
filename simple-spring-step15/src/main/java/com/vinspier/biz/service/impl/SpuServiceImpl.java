package com.vinspier.biz.service.impl;

import com.vinspier.biz.service.SpuService;
import com.vinspier.biz.service.StockService;
import com.vinspier.springframework.beans.annotation.Autowired;
import com.vinspier.springframework.beans.annotation.Qualifier;
import com.vinspier.springframework.beans.annotation.Value;
import com.vinspier.springframework.context.annotation.Scope;
import com.vinspier.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("spuService")
@Scope(value = "singleton")
public class SpuServiceImpl implements SpuService {

    @Autowired
    private StockService stockService;

    @Value("${spu.refreshDate}")
    private LocalDate refreshDate;

    @Override
    public String getSpuName() {
        return "spu name";
    }

    public void printStockService(){
        System.out.printf("代理对象stockService %s%n",stockService);
    }

    @Override
    public LocalDate getDate() {
        return refreshDate;
    }
}
