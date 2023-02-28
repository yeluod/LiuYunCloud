package com.liuyun.auth.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * AuthOauth2Service
 *
 * @author W.d
 * @since 2023/2/27 14:11
 **/
public interface AuthOauth2Service {

    /**
     * 注销登录
     *
     * @param request {@link HttpServletRequest}
     * @author W.d
     * @since 2023/2/27 17:07
     **/
    void logout(HttpServletRequest request);

}
