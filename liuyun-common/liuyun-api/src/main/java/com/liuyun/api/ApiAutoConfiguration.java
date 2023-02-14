package com.liuyun.api;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.liuyun.domain.auth.constants.AuthServerConstant;
import com.liuyun.domain.base.constants.TraceConstants;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * CoreAutoConfig
 *
 * @author W.d
 * @since 2022/12/17 18:02
 **/
@Lazy
@Slf4j
@ComponentScan(basePackages = "com.liuyun.api.*.service.fallback")
@EnableFeignClients(basePackages = "com.liuyun.api.*.service")
public class ApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.NONE;
    }

    @Bean
    @ConditionalOnMissingBean
    public Request.Options options() {
        return new Request.Options(
                3000L, TimeUnit.MILLISECONDS,
                6000L, TimeUnit.MILLISECONDS,
                true);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return template -> Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .ifPresent(request -> {
                    String token = JakartaServletUtil.getHeader(request, AuthServerConstant.AUTHORIZATION_KEY, StandardCharsets.UTF_8);
                    if (log.isDebugEnabled()) {
                        log.info("Feign AUTHORIZATION -> [{}]", token);
                    }
                    template.header(TraceConstants.TRACE_ID, MDC.get(TraceConstants.TRACE_ID));
                    template.header(AuthServerConstant.AUTHORIZATION_KEY, token);
                    template.header(AuthServerConstant.AUTHORIZATION_FEIGN_KEY, AuthServerConstant.AUTHORIZATION_FEIGN_TOKEN);
                });
    }


}
