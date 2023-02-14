package com.liuyun.auth.support.password;

import com.liuyun.auth.support.base.BaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * AbstractBaseAuthenticationToken
 *
 * @author W.d
 * @since 2023/2/7 16:26
 **/
public class PasswordAuthenticationToken extends BaseAuthenticationToken {

    public PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                       Authentication clientPrincipal,
                                       Set<String> scopes,
                                       Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}
