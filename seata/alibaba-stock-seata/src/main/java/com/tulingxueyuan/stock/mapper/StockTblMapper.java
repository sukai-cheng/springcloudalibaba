package com.tulingxueyuan.stock.mapper;


import com.tulingxueyuan.stock.pojo.StockTbl;

public interface StockTblMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByProductId(Long productId);

    int reduct(Long productId);

    int insert(StockTbl record);

    int insertSelective(StockTbl record);

    StockTbl selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockTbl record);

    int updateByPrimaryKey(StockTbl record);

}