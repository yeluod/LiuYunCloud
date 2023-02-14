package com.liuyun.auth.token;

import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Instant;
import java.util.UUID;

/**
 * AuthOauth2TokenGenerator
 *
 * @author W.d
 * @since 2023/2/10 09:37
 **/
public class AuthOauth2AuthorizationCodeGenerator implements OAuth2TokenGenerator<OAuth2AuthorizationCode> {

    /**
     * 生成授权码
     *
     * @param context {@link OAuth2TokenContext} 令牌信息的上下文
     * @return org.springframework.security.oauth2.core.OAuth2AccessToken
     * @author W.d
     * @since 2023/2/9 10:40
     **/
    @Override
    public OAuth2AuthorizationCode generate(OAuth2TokenContext context) {
        if (context.getTokenType() == null ||
            !OAuth2ParameterNames.CODE.equals(context.getTokenType().getValue())) {
            return null;
        }
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(context.getRegisteredClient().getTokenSettings().getAuthorizationCodeTimeToLive());
        return new OAuth2AuthorizationCode(UUID.randomUUID().toString(), issuedAt, expiresAt);
    }

}
