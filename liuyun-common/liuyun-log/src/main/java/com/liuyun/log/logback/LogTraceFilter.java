package com.liuyun.log.logback;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.liuyun.domain.base.constants.TraceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * LogInterceptor
 *
 * @author W.d
 * @since 2022/12/7 22:40
 **/
public class LogTraceFilter extends MDCInsertingServletFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            super.doFilter(request, response, chain);
            return;
        }
        Opt.ofNullable(request)
                .map(HttpServletRequest.class::cast)
                .map(req -> JakartaServletUtil.getHeader(req, TraceConstants.TRACE_ID, StandardCharsets.UTF_8))
                .filter(StrUtil::isNotBlank)
                .or(() -> Opt.of(LogIdGenerator.generateTraceId()))
                .ifPresent(tid -> MDC.put(TraceConstants.TRACE_ID, tid));
        try {
            super.doFilter(request, response, chain);
        } finally {
            MDC.remove(TraceConstants.TRACE_ID);
        }
    }

}
