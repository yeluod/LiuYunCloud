package com.liuyun.auth.support.base;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.repository.token.AuthAuthorizationService;
import com.liuyun.auth.support.password.PasswordOauth2AuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.ScopeNotActiveException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * BaseAuthenticationProvider
 *
 * @author W.d
 * @since 2023/2/7 17:40
 **/
@Slf4j
public abstract class BaseAuthenticationProvider<T extends BaseAuthenticationToken> implements AuthenticationProvider {

    private final AuthAuthorizationService authAuthorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     *
     * @param authAuthorizationService the authorization service
     * @param tokenGenerator           the token generator
     * @since 0.2.3
     */
    protected BaseAuthenticationProvider(AuthenticationManager authenticationManager,
                                         AuthAuthorizationService authAuthorizationService,
                                         OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authAuthorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authAuthorizationService = authAuthorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * 当前的请求客户端是否支持此模式
     *
     * @param registeredClient {@link RegisteredClient}
     * @author W.d
     * @since 2023/2/7 17:50
     **/
    protected abstract void checkClient(RegisteredClient registeredClient);

    /**
     * 身份验证令牌
     *
     * @param reqParameters {@link Map}
     * @return org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     * @author W.d
     * @since 2023/2/8 09:38
     **/
    protected abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        @SuppressWarnings("unchecked") T baseAuthenticationToken = (T) authentication;
        // 获取经过身份验证的客户端
        OAuth2ClientAuthenticationToken clientPrincipal = this.getAuthenticatedClientElseThrowInvalidClient(baseAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        assert registeredClient != null;
        // 检查客户端
        this.checkClient(registeredClient);
        // 检查 scopes
        Set<String> registeredClientScopes = registeredClient.getScopes();
        Assert.isTrue(registeredClientScopes.containsAll(baseAuthenticationToken.getScopes()),
                () -> new OAuth2AuthenticationException(Oauth2Helper.error("scope error")));

        Map<String, Object> reqParameters = baseAuthenticationToken.getAdditionalParameters();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = this.buildToken(reqParameters);

        try {
            Authentication usernamePasswordAuthentication = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(usernamePasswordAuthentication)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(registeredClientScopes)
                    .authorizationGrantType(PasswordOauth2AuthenticationConverter.PASSWORD)
                    .authorizationGrant(baseAuthenticationToken);

            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(usernamePasswordAuthentication.getName())
                    .authorizationGrantType(PasswordOauth2AuthenticationConverter.PASSWORD)
                    .authorizedScopes(registeredClientScopes);

            // ----- Access token -----
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            Assert.notNull(generatedAccessToken, () -> new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "access token 生成错误", null)));
            //noinspection DataFlowIssue
            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
            if (generatedAccessToken instanceof ClaimAccessor claimAccessor) {
                authorizationBuilder.token(accessToken, metadata -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, claimAccessor.getClaims()))
                        .authorizedScopes(registeredClientScopes)
                        .attributes(item -> item.put(Principal.class.getName(), usernamePasswordAuthentication));
            } else {
                authorizationBuilder.id(accessToken.getTokenValue()).accessToken(accessToken);
            }

            // ----- Refresh token -----
            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

                tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                Assert.isTrue((generatedRefreshToken instanceof OAuth2RefreshToken), () -> new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "refresh token 生成错误", null)));
                refreshToken = Opt.ofBlankAble(generatedRefreshToken).map(OAuth2RefreshToken.class::cast).get();
                authorizationBuilder.refreshToken(refreshToken);
            }


            OAuth2Authorization authorization = authorizationBuilder.build();

            this.authAuthorizationService.save(authorization);
            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                    refreshToken, Objects.requireNonNull(authorization.getAccessToken().getClaims()));
        } catch (Exception e) {
            throw convertToOauth2Exception(e);
        }
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    private OAuth2AuthenticationException convertToOauth2Exception(Exception exception) {
        if (exception instanceof UsernameNotFoundException) {
            return new OAuth2AuthenticationException(Oauth2Helper.error("账号或密码不正确"));
        }
        if (exception instanceof BadCredentialsException ex) {
            return new OAuth2AuthenticationException(Oauth2Helper.error(ex.getMessage()));
        }
        if (exception instanceof DisabledException) {
            return new OAuth2AuthenticationException(Oauth2Helper.error("账号已禁用"));
        }
        if (exception instanceof ScopeNotActiveException) {
            return new OAuth2AuthenticationException(Oauth2Helper.error("scope error"));
        }
        return new OAuth2AuthenticationException(Oauth2Helper.error(exception.getMessage()));
    }

}
