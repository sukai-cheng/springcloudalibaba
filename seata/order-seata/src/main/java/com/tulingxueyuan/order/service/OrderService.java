package com.tulingxueyuan.order.service;

import com.tulingxueyuan.order.pojo.OrderTbl;

/**
 * @author chengsukai
 * @since 2022-09-15 12:18
 */
public interface OrderService {
    OrderTbl create(OrderTbl orderTbl);
}
