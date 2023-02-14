package com.liuyun.base.utils;

import cn.hutool.core.lang.Opt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * JakartaServletUtil
 *
 * @author W.d
 * @since 2023/2/13 10:48
 **/
@UtilityClass
public class ServletUtil extends cn.hutool.extra.servlet.JakartaServletUtil {

    /**
     * 获取 HttpServletRequest
     *
     * @return jakarta.servlet.http.HttpServletRequest
     * @author W.d
     * @since 2023/2/13 10:52
     **/
    public HttpServletRequest getRequest() {
        return Opt.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest)
                .orElseThrow(() -> new IllegalArgumentException("Request Is Null"));
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return jakarta.servlet.http.HttpServletResponse
     * @author W.d
     * @since 2023/2/13 10:53
     **/
    public HttpServletResponse getResponse() {
        return Opt.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getResponse)
                .orElseThrow(() -> new IllegalArgumentException("Response Is Null"));
    }

}
