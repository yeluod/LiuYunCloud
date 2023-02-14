package com.liuyun.auth.support.sms;

import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.support.base.BaseOauth2AuthenticationConverter;
import com.liuyun.domain.auth.constants.AuthServerConstant;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
public class PhoneSmsOauth2AuthenticationConverter extends BaseOauth2AuthenticationConverter<PhoneSmsAuthenticationToken> {

    public static final AuthorizationGrantType PHONE_SMS = new AuthorizationGrantType("phone_sms");

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
        return PHONE_SMS.getValue().equals(grantType);
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
        String password = parameters.getFirst(AuthServerConstant.PARAMETER_PHONE_CODE);
        if (!StringUtils.hasText(password) || parameters.get(AuthServerConstant.PARAMETER_PHONE_CODE).size() != 1) {
            throw new OAuth2AuthenticationException(Oauth2Helper.error("验证码不能为空"));
        }
    }

    /**
     * 创建Token
     *
     * @param clientPrincipal      {@link Authentication}
     * @param requestedScopes      {@link Set}
     * @param additionalParameters {@link Map}
     * @return {@link PhoneSmsAuthenticationToken}
     * @author W.d
     * @since 2023/2/7 16:56
     **/
    @Override
    public PhoneSmsAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new PhoneSmsAuthenticationToken(PHONE_SMS, clientPrincipal, requestedScopes, additionalParameters);
    }
}
