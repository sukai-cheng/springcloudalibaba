package com.tulingxueyuan.order.service.impl;

import com.tulingxueyuan.order.feign.StockFeignService;
import com.tulingxueyuan.order.mapper.OrderTblMapper;
import com.tulingxueyuan.order.pojo.OrderTbl;
import com.tulingxueyuan.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author chengsukai
 * @since 2022-09-15 12:20
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderTblMapper orderTblMapper;

    @Autowired
    StockFeignService stockFeignService;

    @GlobalTransactional
    @Override
    public void create(OrderTbl order) {

        orderTblMapper.insert(order);
        String msg = stockFeignService.reduct(order.getId());
    }
}
