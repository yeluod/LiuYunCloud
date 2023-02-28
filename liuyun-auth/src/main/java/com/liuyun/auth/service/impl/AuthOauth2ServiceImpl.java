package com.liuyun.auth.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.repository.token.AuthAuthorizationService;
import com.liuyun.auth.service.AuthOauth2Service;
import com.liuyun.domain.auth.constants.AuthServerConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * AuthOauth2ServiceImpl
 *
 * @author W.d
 * @since 2023/2/27 14:12
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthOauth2ServiceImpl implements AuthOauth2Service {

    private final AuthAuthorizationService authAuthorizationService;

    /**
     * 注销登录
     *
     * @param request {@link HttpServletRequest}
     * @author W.d
     * @since 2023/2/27 17:07
     **/
    @Override
    public void logout(HttpServletRequest request) {
        Opt.ofNullable(request)
                .map(item -> JakartaServletUtil.getHeader(request, AuthServerConstant.AUTHORIZATION_KEY, StandardCharsets.UTF_8))
                .filter(StrUtil::isNotBlank)
                .peek(Oauth2Helper::splitToken)
                .filter(StrUtil::isNotBlank)
                .map(Oauth2Helper::parseToken)
                //.map(item -> )
                .ifPresent(authorization -> {})
        ;

    }
}
