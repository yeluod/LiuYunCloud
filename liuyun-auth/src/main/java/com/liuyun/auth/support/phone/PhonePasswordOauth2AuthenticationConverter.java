package com.liuyun.auth.support.phone;

import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.support.base.BaseOauth2AuthenticationConverter;
import com.liuyun.domain.auth.constants.AuthServerConstant;
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
public class PhonePasswordOauth2AuthenticationConverter extends BaseOauth2AuthenticationConverter<PhonePasswordAuthenticationToken> {

    public static final AuthorizationGrantType PHONE_PASSWORD = new AuthorizationGrantType("phone_password");

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
        return PHONE_PASSWORD.getValue().equals(grantType);
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
        // phone (REQUIRED)
        String username = parameters.getFirst(AuthServerConstant.PARAMETER_PHONE);
        if (!StringUtils.hasText(username) || parameters.get(AuthServerConstant.PARAMETER_PHONE).size() != 1) {
            throw new OAuth2AuthenticationException(Oauth2Helper.error("手机号不能为空"));
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
     * @return {@link PhonePasswordAuthenticationToken}
     * @author W.d
     * @since 2023/2/7 16:56
     **/
    @Override
    public PhonePasswordAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new PhonePasswordAuthenticationToken(PHONE_PASSWORD, clientPrincipal, requestedScopes, additionalParameters);
    }
}
