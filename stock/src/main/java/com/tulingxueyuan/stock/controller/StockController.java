package com.tulingxueyuan.stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengsukai
 * @since 2022-09-08 16:25
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @RequestMapping("reduce")
    public String reduct() {
        System.out.println("扣减库存");
        return "扣减库存";
    }
}
