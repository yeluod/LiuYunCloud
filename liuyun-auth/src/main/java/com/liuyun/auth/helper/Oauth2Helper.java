package com.liuyun.auth.helper;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.liuyun.auth.repository.user.BaseUserDetailsService;
import com.liuyun.base.exception.VerifyException;
import com.liuyun.base.utils.ServletUtil;
import com.liuyun.domain.auth.dto.LoginUser;
import com.liuyun.domain.base.enums.BaseCommonEnum;
import com.liuyun.domain.base.enums.BaseEnableEnum;
import com.liuyun.domain.sys.entity.SysClientEntity;
import com.liuyun.domain.sys.entity.SysUserEntity;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * AuthHelper
 *
 * @author W.d
 * @since 2023/2/10 09:54
 **/
@UtilityClass
public class Oauth2Helper {

    /**
     * 转换为 UserDetails
     *
     * @param entity    {@link SysUserEntity} 用户信息
     * @return org.springframework.security.core.userdetails.User
     * @author W.d
     * @since 2023/2/1 13:12
     **/
    public UserDetails convertToUser(SysUserEntity entity) {
        return new User(
                entity.getUsername(),
                entity.getPassword(),
                StrUtil.equals(BaseEnableEnum.ENABLE.name(), entity.getStatus()),
                true,
                true,
                true,
                Collections.emptyList()
        );
    }

    /**
     * 转换为 RegisteredClient
     *
     * @param entity {@link SysClientEntity} 客户端
     * @return org.springframework.security.oauth2.server.authorization.client.RegisteredClient
     * @author W.d
     * @since 2023/2/9 17:36
     **/
    public RegisteredClient convertToClient(SysClientEntity entity) {
        return RegisteredClient.withId(String.valueOf(entity.getId()))
                .clientId(entity.getClientId())
                .clientName(entity.getClientName())
                .clientSecret(entity.getClientSecret())
                .clientAuthenticationMethods(methods -> methods.addAll(
                        Arrays.asList(ClientAuthenticationMethod.CLIENT_SECRET_POST,
                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)))
                .scopes(scopes -> scopes.addAll(entity.getScopes()))
                .redirectUris(redirectUris -> redirectUris.addAll(entity.getRedirectUris()))
                .authorizationGrantTypes(grantTypes ->
                        grantTypes.addAll(entity.getGrantTypes()
                                .stream()
                                .map(AuthorizationGrantType::new).toList()))
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofSeconds(entity.getAccessTokenTimeout()))
                        .refreshTokenTimeToLive(Duration.ofSeconds(entity.getRefreshTokenTimeout()))
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .authorizationCodeTimeToLive(Duration.ofSeconds(entity.getCodeTimeout()))
                        .reuseRefreshTokens(true)
                        .build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(BaseCommonEnum.Y.name().equals(entity.getAutoApprove())).build())
                .build();
    }

    /**
     * 转换为 LoginUser
     *
     * @param entity {@link SysUserEntity}
     * @return com.liuyun.domain.auth.dto.LoginUser
     * @author W.d
     * @since 2023/2/10 13:21
     **/
    public LoginUser convertToLoginUser(SysUserEntity entity) {
        return new LoginUser()
                .setId(entity.getId())
                .setName(entity.getName())
                .setUsername(entity.getUsername())
                .setEmail(entity.getEmail())
                .setMobile(entity.getPhone())
                .setGender(entity.getGender())
                .setAvatar(entity.getAvatar());
    }

    /**
     * 从 Request 中获取 GRANT_TYPE
     *
     * @return java.lang.String
     * @author W.d
     * @since 2023/2/13 10:58
     **/
    public String getGrantTypeByRequest() {
        return Opt.ofNullable(ServletUtil.getRequest())
                .map(ServletUtil::getParamMap)
                .map(item -> item.get(OAuth2ParameterNames.GRANT_TYPE))
                .orElseThrow(() -> new VerifyException("web request is empty"));
    }

    public BaseUserDetailsService getUserDetailsServiceByGrantType(Map<String, BaseUserDetailsService> userDetailsServices,
                                                                   String grantType) {
        return Opt.ofNullable(userDetailsServices.get(grantType))
                .orElse(userDetailsServices.get("default"));
    }

    public OAuth2Error error(String message) {
        return new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, message, null);
    }
}
