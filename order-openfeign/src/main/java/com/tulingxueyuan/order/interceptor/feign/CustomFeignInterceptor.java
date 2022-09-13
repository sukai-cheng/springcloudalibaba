package com.tulingxueyuan.order.interceptor.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author chengsukai
 * @since 2022-09-13 14:55
 */

public class CustomFeignInterceptor implements RequestInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void apply(RequestTemplate template) {
        String access_token = UUID.randomUUID().toString();
        template.header("Authorization", access_token);
        logger.info("feign拦截器");
    }
}
