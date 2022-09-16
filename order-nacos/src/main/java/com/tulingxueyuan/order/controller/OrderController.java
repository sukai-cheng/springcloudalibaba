package com.tulingxueyuan.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author chengsukai
 * @since 2022-09-08 16:24
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/add")
    public String add() {
        System.out.println("下单成功");
        String message = restTemplate.getForObject("http://stock-service/stock/reduce", String.class);
        return "order" + message;
    }

    @RequestMapping("/flow")
//    @SentinelResource(value = "flow",blockHandler = "flowBlockHandler")
    public String flow() throws InterruptedException {
        return "正常访问";
    }

    @RequestMapping("/get")
    public String get() throws InterruptedException {
        return "查询订单";
    }

//    public String flowBlockHandler(BlockException blockException){
//        return "流控";
//    }

    @RequestMapping("/flowThread")
    public String flowThread() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        return "正常访问";
    }

    @GetMapping("/error")
    public String error() throws InterruptedException{
        int i = 1 / 0;
        return "error";
    }
}
