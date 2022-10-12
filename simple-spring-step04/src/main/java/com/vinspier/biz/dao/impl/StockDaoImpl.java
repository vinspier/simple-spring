package com.vinspier.biz.dao.impl;

import com.vinspier.biz.dao.StockDao;

import java.util.HashMap;
import java.util.Map;

public class StockDaoImpl implements StockDao {

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
}
