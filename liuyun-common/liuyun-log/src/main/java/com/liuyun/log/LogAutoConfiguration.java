package com.liuyun.log;

import com.liuyun.log.logback.LogTraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

/**
 * LogAutoConfiguration
 *
 * @author W.d
 * @since 2022/12/7 22:16
 **/
@Lazy
public class LogAutoConfiguration {

    @Bean
    @Order(Integer.MIN_VALUE)
    public LogTraceFilter logTraceFilter() {
        return new LogTraceFilter();
    }

}
