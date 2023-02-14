package com.liuyun.auth.support.password;

import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.support.base.BaseOauth2AuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * PasswordOauth2AuthenticationConverter
 *
 * @author W.d
 * @since 2023/2/7 16:00
 **/
public class PasswordOauth2AuthenticationConverter extends BaseOauth2AuthenticationConverter<PasswordAuthenticationToken> {

    public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");

    /**
     * 是否支持此授权类型
     *
     * @param grantType {@link String} 授权类型
     * @return boolean
     * @author W.d
     * @since 2023/2/7 16:02
     **/
    @Override
    protected boolean support(String grantType) {
        return PASSWORD.getValue().equals(grantType);
    }

    /**
     * 校验参数
     *
     * @param parameters {@link MultiValueMap}
     * @author W.d
     * @since 2023/2/7 16:16
     **/
    @Override
    protected void checkParams(MultiValueMap<String, String> parameters) {
        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException(Oauth2Helper.error("账号不能为空"));
        }
        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException(Oauth2Helper.error("密码不能为空"));
        }
    }

    /**
     * 创建Token
     *
     * @param clientPrincipal      {@link Authentication}
     * @param requestedScopes      {@link Set}
     * @param additionalParameters {@link Map}
     * @return {@link PasswordAuthenticationToken}
     * @author W.d
     * @since 2023/2/7 16:56
     **/
    @Override
    public PasswordAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new PasswordAuthenticationToken(PASSWORD, clientPrincipal, requestedScopes, additionalParameters);
    }
}
