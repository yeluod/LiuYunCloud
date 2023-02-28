package com.liuyun.auth.token;

import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;

import java.time.Instant;

/**
 * AuthOauth2RefreshTokenGenerator
 *
 * @author W.d
 * @since 2023/2/9 11:45
 **/
public class AuthOauth2RefreshTokenGenerator extends BaseOauth2TokenGenerator<OAuth2RefreshToken> {

    /**
     * 生成令牌
     *
     * @param context {@link OAuth2TokenContext} 令牌信息的上下文
     * @return org.springframework.security.oauth2.core.OAuth2AccessToken
     * @author W.d
     * @since 2023/2/9 10:40
     **/
    @Override
    public OAuth2RefreshToken generate(OAuth2TokenContext context) {
        if (!OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
            return null;
        }
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getRefreshTokenTimeToLive());
        return new OAuth2RefreshToken(super.uuid(), issuedAt, expiresAt);
    }

}
