package com.tulingxueyuan.stock.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author chengsukai
 * @since 2022-09-08 16:25
 */
@RestController
@RequestMapping("/stock")
public class StockController {

    @Value("${server.port}")
    String port;

    @RequestMapping("reduce")
    public String reduct() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5000);
        System.out.println("扣减库存");
        return "扣减库存" + port;
    }
}
