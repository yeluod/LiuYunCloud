package com.liuyun.auth.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

/**
 * 自定义访问令牌
 *
 * @author W.d
 * @since 2023/2/9 10:36
 **/
@EqualsAndHashCode(callSuper = true)
public class AuthOauth2AccessTokenClaims extends OAuth2AccessToken implements ClaimAccessor {

    @Getter
    @SuppressWarnings("all")
    private final Map<String, Object> claims;

    /**
     * 构建令牌
     *
     * @param tokenType  {@link TokenType} 令牌类型
     * @param tokenValue {@link String} 令牌
     * @param issuedAt   {@link Instant} 颁发日期
     * @param expiresAt  {@link Instant} 过期日期
     * @param scopes     {@link Set<String>} 作用域
     * @param claims     {@link Map}  荷载数据
     * @author W.d
     * @since 2023/2/9 11:03
     **/
    public AuthOauth2AccessTokenClaims(TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt,
                                       Set<String> scopes, Map<String, Object> claims) {
        super(tokenType, tokenValue, issuedAt, expiresAt, scopes);
        this.claims = claims;
    }

}
