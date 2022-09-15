package com.tulingxueyuan.order.controller;

import com.tulingxueyuan.order.pojo.OrderTbl;
import com.tulingxueyuan.order.service.OrderService;
import com.tulingxueyuan.order.service.impl.OrderServiceImpl;
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

    @Autowired
    RestTemplate restTemplate;

    @Resource
    OrderService orderService;

    @RequestMapping("/add")
    public String add() {
        OrderTbl order = new OrderTbl();
        order.setId(0L);
        order.setOrderStatus(0);
        order.setTotalAmount(100);

        orderService.create(order);
        return "下单成功";
    }
}
