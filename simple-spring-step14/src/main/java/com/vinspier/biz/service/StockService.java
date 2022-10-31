package com.vinspier.biz.service;

public interface StockService {

    Long getStock(Long skuId);

    String getWarehouse();

    void setWarehouse(String warehouse);

    void printSpuService();

}
