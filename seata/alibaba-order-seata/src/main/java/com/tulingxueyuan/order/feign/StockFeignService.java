package com.tulingxueyuan.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chengsukai
 * @since 2022-09-13 14:17
 */
@FeignClient(name = "alibaba-stock-seata", path = "/stock")
public interface StockFeignService {
    // 声明需要调用的rest接口对应的方法
    @PostMapping ("/reduct")
    String reduct(@RequestParam(value = "productId") Long productId);

}
