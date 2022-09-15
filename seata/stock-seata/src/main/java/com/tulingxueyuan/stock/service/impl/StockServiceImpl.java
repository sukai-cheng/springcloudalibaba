package com.tulingxueyuan.stock.service.impl;


import com.tulingxueyuan.stock.mapper.StockTblMapper;
import com.tulingxueyuan.stock.service.StockService;

import javax.annotation.Resource;

/**
 * @author chengsukai
 * @since 2022-09-15 12:48
 */
public class StockServiceImpl implements StockService {
    @Resource
    StockTblMapper stockTblMapper;

    @Override
    public void reduct(Long productId) {
        System.out.println("更新商品: " + productId);
        stockTblMapper.reduct(productId);
    }
}
