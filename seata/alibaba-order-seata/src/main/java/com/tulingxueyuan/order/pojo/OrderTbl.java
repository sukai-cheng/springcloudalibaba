package com.tulingxueyuan.order.pojo;

public class OrderTbl {
    private Long id;

    private Integer orderStatus;

    private Integer totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer status) {
        this.orderStatus = status;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }
}