package com.liuyun.auth.config;

import com.liuyun.auth.repository.token.AuthAuthorizationService;
import com.liuyun.auth.support.core.AuthUserDetailsAuthenticationProvider;
import com.liuyun.auth.support.password.PasswordAuthenticationProvider;
import com.liuyun.auth.support.password.PasswordOauth2AuthenticationConverter;
import com.liuyun.auth.support.phone.PhonePasswordAuthenticationProvider;
import com.liuyun.auth.support.phone.PhonePasswordOauth2AuthenticationConverter;
import com.liuyun.auth.support.sms.PhoneSmsAuthenticationProvider;
import com.liuyun.auth.support.sms.PhoneSmsOauth2AuthenticationConverter;
import com.liuyun.auth.token.AuthOauth2AccessTokenGenerator;
import com.liuyun.auth.token.AuthOauth2RefreshTokenGenerator;
import com.liuyun.auth.token.AuthOauth2TokenCustomizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Arrays;

/**
 * AuthorizationServerConfiguration
 *
 * @author W.d
 * @since 2022/12/22 11:34
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

    private final AuthAuthorizationService authAuthorizationService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        var authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        http.apply(authorizationServerConfigurer
                // 个性化认证授权端点
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        // 添加自定义请求转换器
                        .accessTokenRequestConverter(
                                new DelegatingAuthenticationConverter(Arrays.asList(
                                        new PasswordOauth2AuthenticationConverter(),
                                        new PhonePasswordOauth2AuthenticationConverter(),
                                        new PhoneSmsOauth2AuthenticationConverter(),
                                        new OAuth2RefreshTokenAuthenticationConverter(),
                                        new OAuth2ClientCredentialsAuthenticationConverter(),
                                        new OAuth2AuthorizationCodeAuthenticationConverter(),
                                        new OAuth2AuthorizationCodeRequestAuthenticationConverter()))
                        )
                        // 登录成功处理器
                        .accessTokenResponseHandler(authenticationSuccessHandler)
                        // 登录失败处理器
                        .errorResponseHandler(authenticationFailureHandler))
                .clientAuthentication(oAuth2ClientAuthenticationConfigurer ->
                        // 身份验证失败处理
                        oAuth2ClientAuthenticationConfigurer.errorResponseHandler(authenticationFailureHandler)
                ));
        var endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        var securityFilterChain = http.securityMatcher(endpointsMatcher)
                // redis存储token的实现
                .apply(authorizationServerConfigurer.authorizationService(this.authAuthorizationService)
                        .authorizationServerSettings(AuthorizationServerSettings.builder().build()))
                .and()
                .headers().frameOptions().disable().and().csrf().disable()
                .build();

        // 添加自定义授权模式
        var authenticationManager = http.getSharedObject(AuthenticationManager.class);
        // 账号 + 密码模式
        var passwordAuthenticationProvider =
                new PasswordAuthenticationProvider(authenticationManager, this.authAuthorizationService, oauth2TokenGenerator());
        var authenticationProvider = new AuthUserDetailsAuthenticationProvider();
        // 手机号 + 密码
        var phonePasswordAuthenticationProvider =
                new PhonePasswordAuthenticationProvider(authenticationManager, this.authAuthorizationService, oauth2TokenGenerator());

        // 手机号 + 验证码
        var phoneSmsAuthenticationProvider =
                new PhoneSmsAuthenticationProvider(authenticationManager, this.authAuthorizationService, oauth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(authenticationProvider);
        http.authenticationProvider(passwordAuthenticationProvider);
        http.authenticationProvider(phonePasswordAuthenticationProvider);
        http.authenticationProvider(phoneSmsAuthenticationProvider);
        return securityFilterChain;
    }

    /**
     * 自定义令牌生成器
     *
     * @param {@link }
     * @return OAuth2TokenGenerator
     * @author W.d
     * @since 2023/2/9 11:19
     **/
    @Bean
    @SuppressWarnings("all")
    public OAuth2TokenGenerator<? extends OAuth2Token> oauth2TokenGenerator() {
        // accessToken
        var accessTokenGenerator = new AuthOauth2AccessTokenGenerator();
        accessTokenGenerator.setAccessTokenCustomizer(new AuthOauth2TokenCustomizer());
        // refreshToken
        var refreshTokenGenerator = new AuthOauth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator);
    }


}
