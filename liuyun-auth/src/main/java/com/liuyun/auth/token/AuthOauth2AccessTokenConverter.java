package com.liuyun.auth.token;

import com.liuyun.domain.auth.constants.AuthServerConstant;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * AuthOauth2AccessTokenResponseMapConverter
 *
 * @author W.d
 * @since 2023/2/9 12:17
 **/
public class AuthOauth2AccessTokenConverter implements Converter<OAuth2AccessTokenResponse, Map<String, Object>> {
    /**
     * 转换响应令牌
     *
     * @param tokenResponse {@link OAuth2AccessTokenResponse}
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author W.d
     * @since 2023/2/9 12:23
     **/
    @Override
    public Map<String, Object> convert(OAuth2AccessTokenResponse tokenResponse) {
        Map<String, Object> parameters = new HashMap<>(16);
        parameters.put(OAuth2ParameterNames.ACCESS_TOKEN, tokenResponse.getAccessToken().getTokenValue());
        parameters.put(OAuth2ParameterNames.TOKEN_TYPE, tokenResponse.getAccessToken().getTokenType().getValue());
        parameters.put(OAuth2ParameterNames.EXPIRES_IN, this.getExpiresIn(tokenResponse));
        if (!CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
            parameters.put(OAuth2ParameterNames.SCOPE, StringUtils.collectionToDelimitedString(tokenResponse.getAccessToken().getScopes(), " "));
        }
        if (tokenResponse.getRefreshToken() != null) {
            parameters.put(OAuth2ParameterNames.REFRESH_TOKEN, tokenResponse.getRefreshToken().getTokenValue());
        }
        Map<String, Object> additionalParameters = tokenResponse.getAdditionalParameters();
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            parameters.put(AuthServerConstant.TOKEN_CLAIMS_USER_EXTEND, additionalParameters.get(AuthServerConstant.TOKEN_CLAIMS_USER_EXTEND));
            parameters.put(AuthServerConstant.TOKEN_CLAIMS_ROLE_EXTEND, additionalParameters.get(AuthServerConstant.TOKEN_CLAIMS_ROLE_EXTEND));
        }
        return parameters;
    }

    private long getExpiresIn(OAuth2AccessTokenResponse tokenResponse) {
        OAuth2AccessToken accessToken = tokenResponse.getAccessToken();
        if (accessToken != null) {
            return ChronoUnit.SECONDS.between(Objects.requireNonNull(accessToken.getIssuedAt()), accessToken.getExpiresAt());
        }
        return -1L;
    }

}
