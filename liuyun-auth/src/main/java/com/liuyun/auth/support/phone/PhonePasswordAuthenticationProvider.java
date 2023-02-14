package com.liuyun.auth.support.phone;

import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.repository.token.AuthAuthorizationService;
import com.liuyun.auth.support.base.BaseAuthenticationProvider;
import com.liuyun.domain.auth.constants.AuthServerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * BaseAuthenticationProvider
 *
 * @author W.d
 * @since 2023/2/7 17:40
 **/
@Slf4j
public class PhonePasswordAuthenticationProvider extends BaseAuthenticationProvider<PhonePasswordAuthenticationToken> {

    public PhonePasswordAuthenticationProvider(AuthenticationManager authenticationManager,
                                               AuthAuthorizationService authAuthorizationService,
                                               OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authAuthorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = PhonePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    /**
     * 当前的请求客户端是否支持此模式
     *
     * @param registeredClient {@link RegisteredClient}
     * @author W.d
     * @since 2023/2/7 17:50
     **/
    @Override
    protected void checkClient(RegisteredClient registeredClient) {
        if (!registeredClient.getAuthorizationGrantTypes().contains(PhonePasswordOauth2AuthenticationConverter.PHONE_PASSWORD)) {
            throw new OAuth2AuthenticationException(Oauth2Helper.error("当前客户端暂不支持此认证模式"));
        }
    }

    /**
     * 身份验证令牌
     *
     * @param reqParameters {@link Map}
     * @return org.springframework.security.authentication.UsernamePasswordAuthenticationToken
     * @author W.d
     * @since 2023/2/8 09:38
     **/
    @Override
    protected UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String username = (String) reqParameters.get(AuthServerConstant.PARAMETER_PHONE);
        String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
