package com.tulingxueyuan.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author chengsukai
 * @since 2022-09-08 16:24
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @RequestMapping("/add")
    public String add() {
        System.out.println("下单成功");
        return "Hello World";
    }

    @RequestMapping("/flow")
//    @SentinelResource(value = "flow",blockHandler = "flowBlockHandler")
    public String flow() throws InterruptedException {
        return "正常访问";
    }

//    public String flowBlockHandler(BlockException blockException){
//        return "流控";
//    }
}
