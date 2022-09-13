package com.tulingxueyuan.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chengsukai
 * @since 2022-09-13 14:17
 */
@FeignClient(name = "stock-service",path = "/stock")
public interface StockFeignService {
    // 声明需要调用的rest接口对应的方法
    @RequestMapping("reduce")
    String reduct();

}
