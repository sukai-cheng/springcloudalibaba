package com.tulingxueyuan.order.controller;

import com.tulingxueyuan.order.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author chengsukai
 * @since 2022-09-08 16:24
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    StockFeignService stockFeignService;

    @RequestMapping("/add")
    public String add() {
        System.out.println("下单成功");
        String msg = stockFeignService.reduct();
        return "Hello Feign" + msg;
    }
}
