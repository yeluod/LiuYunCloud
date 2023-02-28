package com.liuyun.auth.token;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.extra.spring.SpringUtil;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.repository.token.AuthAuthorizationService;
import com.liuyun.auth.repository.user.BaseUserDetailsService;
import com.liuyun.auth.service.SysPermissionService;
import com.liuyun.auth.service.SysRoleService;
import com.liuyun.base.dto.BaseDTO;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;

/**
 * AuthOAuth2TokenGenerator
 *
 * @author W.d
 * @since 2023/2/8 14:09
 **/
public class AuthOauth2AccessTokenGenerator extends BaseOauth2TokenGenerator<OAuth2AccessToken> {

    private final AuthOauth2TokenCustomizer accessTokenCustomizer;
    private final AuthAuthorizationService authAuthorizationService;
    private final SysRoleService sysRoleService;
    private final SysPermissionService sysPermissionService;
    private final Map<String, BaseUserDetailsService> userDetailsServices;

    public AuthOauth2AccessTokenGenerator(AuthOauth2TokenCustomizer accessTokenCustomizer, AuthAuthorizationService authAuthorizationService) {
        this.accessTokenCustomizer = accessTokenCustomizer;
        this.authAuthorizationService = authAuthorizationService;
        this.sysRoleService = SpringUtil.getBeansOfType(SysRoleService.class).entrySet().iterator().next().getValue();
        this.sysPermissionService = SpringUtil.getBeansOfType(SysPermissionService.class).entrySet().iterator().next().getValue();
        this.userDetailsServices = SpringUtil.getBeansOfType(BaseUserDetailsService.class);
    }

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
        AuthorizationGrantType grantType = context.getAuthorizationGrantType();
        if (!Objects.equals(AuthorizationGrantType.CLIENT_CREDENTIALS, grantType)) {
            var userService = Oauth2Helper.getUserDetailsServiceByGrantType(userDetailsServices, grantType.getValue());
            var user = (User) context.getPrincipal().getPrincipal();
            var username = user.getUsername();
            var userEntity = userService.getUser(username);
            var loginUser = Oauth2Helper.convertToLoginUser(userEntity);
            Set<String> roles = this.sysRoleService.queryRoleCodesByUserId(userEntity.getId());
            List<Tree<Long>> menus = this.sysPermissionService.loadMenusByUserId(userEntity.getId());
            this.accessTokenCustomizer.customize(accessTokenContextBuilder.build(), loginUser, roles, menus);
            this.authAuthorizationService.saveUser(userEntity.getId(), loginUser, roles, menus);
            OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
            return new AuthOauth2AccessTokenClaims(
                    OAuth2AccessToken.TokenType.BEARER,
                    super.jwt(BaseDTO.create().set("id", loginUser.getId())),
                    accessTokenClaimsSet.getIssuedAt(),
                    accessTokenClaimsSet.getExpiresAt(),
                    context.getAuthorizedScopes(),
                    accessTokenClaimsSet.getClaims());
        } else {
            OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
            return new AuthOauth2AccessTokenClaims(
                    OAuth2AccessToken.TokenType.BEARER,
                    super.uuid(),
                    accessTokenClaimsSet.getIssuedAt(),
                    accessTokenClaimsSet.getExpiresAt(),
                    context.getAuthorizedScopes(),
                    accessTokenClaimsSet.getClaims());
        }
    }
}
