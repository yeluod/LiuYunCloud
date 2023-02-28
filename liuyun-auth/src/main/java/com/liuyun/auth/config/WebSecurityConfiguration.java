package com.liuyun.auth.config;

import com.liuyun.auth.support.core.AuthUserDetailsAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * WebSecurityConfiguration
 *
 * @author W.d
 * @since 2023/2/1 16:23
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    /**
     * 这个也是个Spring Security的过滤器链，用于Spring Security的身份认证。
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        // 开放自定义的部分端点
        http.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/token/logout",
                                "/code/sms/**",
                                "/doc.html",
                                "/favicon.ico",
                                "/webjars/js/**",
                                "/webjars/css/**",
                                "/v3/api-docs/**",
                                "/actuator/**"
                        )
                        .permitAll()
                        .anyRequest().authenticated());
        http.authenticationProvider(new AuthUserDetailsAuthenticationProvider());
        return http.build();
    }

}
