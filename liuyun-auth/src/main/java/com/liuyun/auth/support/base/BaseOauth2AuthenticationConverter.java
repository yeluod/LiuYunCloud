package com.liuyun.auth.support.base;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Oauth2BaseAuthenticationConverter
 *
 * @author W.d
 * @since 2023/2/7 15:58
 **/
public abstract class BaseOauth2AuthenticationConverter<T extends BaseAuthenticationToken> implements AuthenticationConverter {

    /**
     * 是否支持此授权类型
     *
     * @param grantType {@link String} 授权类型
     * @return boolean
     * @author W.d
     * @since 2023/2/7 16:02
     **/
    protected abstract boolean support(String grantType);

    /**
     * 校验参数
     *
     * @param parameters {@link MultiValueMap}
     * @author W.d
     * @since 2023/2/7 16:16
     **/
    protected void checkParams(MultiValueMap<String, String> parameters) {
    }

    /**
     * 创建Token
     *
     * @param clientPrincipal      {@link Authentication}
     * @param requestedScopes      {@link Set}
     * @param additionalParameters {@link Map}
     * @return {@link BaseAuthenticationToken}
     * @author W.d
     * @since 2023/2/7 16:56
     **/
    public abstract T buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

    @Override
    public Authentication convert(HttpServletRequest request) {

        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!support(grantType)) {
            return null;
        }
        MultiValueMap<String, String> parameters = this.getParameters(request);
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);

        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            throw new OAuth2AuthenticationException("scope 错误");
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }
        // 校验个性化参数
        checkParams(parameters);

        // 获取当前已经认证的客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(clientPrincipal)) {
            throw new OAuth2AuthenticationException("客户端身份验证失败");
        }

        // 扩展信息
        Map<String, Object> additionalParameters = parameters.entrySet()
                .stream()
                .filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
                             && !e.getKey().equals(OAuth2ParameterNames.SCOPE))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        // 创建token
        return buildToken(clientPrincipal, requestedScopes, additionalParameters);
    }

    protected MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

}
