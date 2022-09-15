package com.tulingxueyuan.order.controller;

import com.tulingxueyuan.order.pojo.OrderTbl;
import com.tulingxueyuan.order.service.OrderService;
import com.tulingxueyuan.order.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    RestTemplate restTemplate;

    @Resource
    OrderServiceImpl orderServiceImpl;

    @GetMapping("/add")
    public String add() {
        OrderTbl order = new OrderTbl();
        order.setId(1L);
        order.setOrderStatus(0);
        order.setTotalAmount(100);

        orderServiceImpl.create(order);
        return "下单成功";
    }
}
