package com.tulingxueyuan.order.feign;

import org.springframework.stereotype.Component;

/**
 * @author chengsukai
 * @since 2022-09-14 15:07
 */
@Component
public class StockFeignServiceFallback implements StockFeignService {
    @Override
    public String reduct() {
        return "降级啦 ！";
    }
}
