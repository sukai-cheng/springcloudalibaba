package com.tulingxueyuan.order.service.impl;

import com.tulingxueyuan.order.feign.StockFeignService;
import com.tulingxueyuan.order.mapper.OrderTblMapper;
import com.tulingxueyuan.order.pojo.OrderTbl;
import com.tulingxueyuan.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.apache.skywalking.apm.toolkit.trace.Trace;
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
    @Trace
    @Tags({@Tag(key = "add", value = "returnedObj"), @Tag(key = "add", value = "arg[0]")})
    @Override
    public void create(OrderTbl order) {

        orderTblMapper.insert(order);
        String msg = stockFeignService.reduct(1L);
    }
}
