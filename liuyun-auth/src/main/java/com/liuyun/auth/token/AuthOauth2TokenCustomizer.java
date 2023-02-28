package com.liuyun.auth.token;

import com.liuyun.domain.auth.constants.AuthServerConstant;
import com.liuyun.domain.auth.dto.LoginUser;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Set;

/**
 * AuthOauth2TokenCustomizer
 *
 * @author W.d
 * @since 2023/2/8 14:12
 **/
public class AuthOauth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    /**
     * 自定义令牌荷载数据
     *
     * @param context {@link OAuth2TokenClaimsContext} 令牌信息的上下文
     * @author W.d
     * @since 2023/2/9 11:01
     **/
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        // ..
    }

    public void customize(OAuth2TokenClaimsContext context, LoginUser loginUser, Set<String> roles) {
        var claims = context.getClaims();
        claims.claim(AuthServerConstant.TOKEN_CLAIMS_USER_EXTEND, loginUser);
        claims.claim(AuthServerConstant.TOKEN_CLAIMS_ROLE_EXTEND, roles);
    }
}
