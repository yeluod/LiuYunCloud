package com.liuyun.auth.config;

import cn.hutool.core.lang.Opt;
import com.liuyun.auth.token.AuthOauth2AccessTokenConverter;
import com.liuyun.base.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.CollectionUtils;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * AuthorizationHandleConfiguration
 *
 * @author W.d
 * @since 2023/2/7 09:58
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizationHandleConfiguration {

    private static final Converter<OAuth2AccessTokenResponse, Map<String, Object>> ACCESS_TOKEN_CONVERTER =
            new AuthOauth2AccessTokenConverter();

    /**
     * 身份验证失败处理
     *
     * @return org.springframework.security.web.authentication.AuthenticationFailureHandler
     * @author W.d
     * @since 2023/2/7 10:12
     **/
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            Result.write(response, Result.fail(exception.getMessage()));
        };
    }

    /**
     * 身份验证成功处理
     *
     * @return org.springframework.security.web.authentication.AuthenticationSuccessHandler
     * @author W.d
     * @since 2023/2/7 10:31
     **/
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            this.sendAccessTokenResponse(response, authentication);
        };
    }

    /**
     * 输出令牌
     *
     * @param response       {@link HttpServletResponse}
     * @param authentication {@link Authentication}
     * @author W.d
     * @since 2023/2/7 17:17
     **/
    private void sendAccessTokenResponse(HttpServletResponse response, Authentication authentication) {
        var accessTokenAuthentication = Opt.ofNullable(authentication)
                .filter(OAuth2AccessTokenAuthenticationToken.class::isInstance)
                .map(OAuth2AccessTokenAuthenticationToken.class::cast)
                .get();

        var accessToken = accessTokenAuthentication.getAccessToken();
        var refreshToken = accessTokenAuthentication.getRefreshToken();
        var additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        var builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        var accessTokenResponse = builder.build();
        // 无状态 注意删除 context 上下文的信息
        SecurityContextHolder.clearContext();
        var convert = ACCESS_TOKEN_CONVERTER.convert(accessTokenResponse);
        Result.write(response, Result.success(convert));
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                System.out.println("123123123213");
            }
        };
    }
}
