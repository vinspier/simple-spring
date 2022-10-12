package com.vinspier.biz.service.impl;

import com.vinspier.biz.dao.StockDao;
import com.vinspier.biz.service.StockService;

public class StockServiceImpl implements StockService {

    private StockDao stockDao;

    private String warehouse;

    @Override
    public Long getStock(Long skuId) {
        return stockDao.getStock(skuId);
    }

}
