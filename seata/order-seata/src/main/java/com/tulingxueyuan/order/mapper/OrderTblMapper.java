package com.tulingxueyuan.order.mapper;


import com.tulingxueyuan.order.pojo.OrderTbl;

public interface OrderTblMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderTbl record);

    int insertSelective(OrderTbl record);

    OrderTbl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderTbl record);

    int updateByPrimaryKey(OrderTbl record);
}