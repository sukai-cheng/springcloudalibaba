package com.tulingxueyuan.stock.controller;

import com.tulingxueyuan.stock.service.impl.StockServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chengsukai
 * @since 2022-09-08 16:24
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @Resource
    StockServiceImpl stockServiceImpl;

    @PostMapping("/reduct")
    public String reduct(@RequestParam(value = "productId") Long productId) {
        stockServiceImpl.reduct(productId);
        return "扣减库存";
    }
}
