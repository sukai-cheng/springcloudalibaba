package com.tulingxueyuan.stock.controller;

import com.tulingxueyuan.stock.service.StockService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author chengsukai
 * @since 2022-09-08 16:24
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @Resource
    StockService stockService;

    @PostMapping("/reduct")
    public String reduct(@RequestParam(value = "productId") Long productId) {
        stockService.reduct(productId);
        return "扣减库存";
    }
}
