package com.liuyun.auth.token;

import cn.hutool.core.lang.Opt;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

/**
 * AuthOAuth2TokenGenerator
 *
 * @author W.d
 * @since 2023/2/8 14:09
 **/
public class AuthOauth2AccessTokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

    private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;

    /**
     * 生成令牌
     *
     * @param context {@link OAuth2TokenContext} 令牌信息的上下文
     * @return org.springframework.security.oauth2.core.OAuth2AccessToken
     * @author W.d
     * @since 2023/2/9 10:40
     **/
    @Override
    public OAuth2AccessToken generate(OAuth2TokenContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) ||
            !OAuth2TokenFormat.REFERENCE.equals(context.getRegisteredClient().getTokenSettings().getAccessTokenFormat())) {
            return null;
        }

        OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
        // 颁发者
        Opt.of(context)
                .map(OAuth2TokenContext::getAuthorizationServerContext)
                .map(AuthorizationServerContext::getIssuer)
                .filter(StringUtils::hasText)
                .ifPresent(claimsBuilder::issuer);

        RegisteredClient registeredClient = context.getRegisteredClient();
        // 颁发时间
        Instant issuedAt = Instant.now();
        // 过期时间
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

        claimsBuilder
                .subject(context.getPrincipal().getName())
                .audience(Collections.singletonList(registeredClient.getClientId()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .notBefore(issuedAt)
                .id(UUID.randomUUID().toString());
        // scopes
        Opt.ofEmptyAble(context.getAuthorizedScopes())
                .ifPresent(scopes -> claimsBuilder.claim(OAuth2ParameterNames.SCOPE, scopes));

        if (Objects.nonNull(this.accessTokenCustomizer)) {
            OAuth2TokenClaimsContext.Builder accessTokenContextBuilder = OAuth2TokenClaimsContext.with(claimsBuilder)
                    .registeredClient(context.getRegisteredClient())
                    .principal(context.getPrincipal())
                    .authorizationServerContext(context.getAuthorizationServerContext())
                    .authorizedScopes(context.getAuthorizedScopes())
                    .tokenType(context.getTokenType())
                    .authorizationGrantType(context.getAuthorizationGrantType());
            if (Objects.nonNull(context.getAuthorization())) {
                accessTokenContextBuilder.authorization(context.getAuthorization());
            }
            if (Objects.nonNull(context.getAuthorizationGrant())) {
                accessTokenContextBuilder.authorizationGrant(context.getAuthorizationGrant());
            }
            this.accessTokenCustomizer.customize(accessTokenContextBuilder.build());
        }

        OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
        return new AuthOauth2AccessTokenClaims(
                OAuth2AccessToken.TokenType.BEARER,
                UUID.randomUUID().toString(),
                accessTokenClaimsSet.getIssuedAt(),
                accessTokenClaimsSet.getExpiresAt(),
                context.getAuthorizedScopes(),
                accessTokenClaimsSet.getClaims());
    }

    /**
     * 设置定制器
     *
     * @param accessTokenCustomizer {@link OAuth2TokenCustomizer}
     * @author W.d
     * @since 2023/2/9 11:14
     **/
    public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
        Assert.notNull(accessTokenCustomizer, "accessTokenCustomizer cannot be null");
        this.accessTokenCustomizer = accessTokenCustomizer;
    }
}
