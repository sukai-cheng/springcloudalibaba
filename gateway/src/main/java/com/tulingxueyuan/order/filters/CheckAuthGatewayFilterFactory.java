package com.tulingxueyuan.order.filters;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.util.List;


/**
 * @author chengsukai
 * @since 2022-09-16 10:13
 */
@Component
@Slf4j
public class CheckAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<CheckAuthGatewayFilterFactory.Config> {

    /**
     * Status key.
     */
    public static final String STATUS_KEY = "status";

    /**
     * URL key.
     */
    public static final String URL_KEY = "url";

    public CheckAuthGatewayFilterFactory() {
        super(CheckAuthGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return List.of("value");
    }

    @Override
    public GatewayFilter apply(CheckAuthGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            // 请求需要带上name这个参数
            String name = exchange.getRequest().getQueryParams().getFirst("name");
            if (StringUtils.isNotBlank(name)) {
                if (config.getValue().equals(name)) {
                    return chain.filter(exchange);
                }else {
                    exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                    return exchange.getResponse().setComplete();
                }
            }
            // 如果没有带也正常访问
           return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {

        private String value;


    }
}
