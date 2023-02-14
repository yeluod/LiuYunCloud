package com.liuyun.auth.support.base;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * AbstractBaseAuthenticationToken
 *
 * @author W.d
 * @since 2023/2/7 16:26
 **/
public abstract class BaseAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    protected final AuthorizationGrantType authorizationGrantType;

    @Getter
    protected final Authentication clientPrincipal;

    @Getter
    protected final Set<String> scopes;

    @Getter
    protected final Map<String, Object> additionalParameters;

    protected BaseAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                      Authentication clientPrincipal,
                                      Set<String> scopes,
                                      Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = scopes;
        this.additionalParameters = additionalParameters;
    }

    /**
     * 扩展模式一般不需要密码
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * 获取用户名
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }
}
