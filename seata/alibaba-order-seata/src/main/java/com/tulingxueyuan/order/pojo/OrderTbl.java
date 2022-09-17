package com.tulingxueyuan.order.pojo;

import lombok.Data;

@Data
public class OrderTbl {
    private Long id;

    private Integer orderStatus;

    private Integer totalAmount;
}