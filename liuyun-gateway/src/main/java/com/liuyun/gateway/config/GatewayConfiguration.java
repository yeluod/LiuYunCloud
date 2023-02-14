package com.liuyun.gateway.config;

import com.liuyun.gateway.filter.RequestLoggerFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * GatewayConfig
 *
 * @author W.d
 * @since 2022/12/22 21:48
 **/
@Lazy
@Slf4j
@Configuration
public class GatewayConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RequestLoggerFilter requestLoggerFilter() {
        return new RequestLoggerFilter();
    }

}
