package com.tulingxueyuan.order.service.impl;

import com.tulingxueyuan.order.mapper.OrderTblMapper;
import com.tulingxueyuan.order.pojo.OrderTbl;
import com.tulingxueyuan.order.service.OrderService;
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

    @Resource
    RestTemplate restTemplate;

    @Transactional
    @Override
    public void create(OrderTbl order) {

        orderTblMapper.insert(order);
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("productId", order.getId());
        String msg = restTemplate.postForObject("http://alibaba-stock-seata/stock/reduct", paramMap, String.class);
    }
}
