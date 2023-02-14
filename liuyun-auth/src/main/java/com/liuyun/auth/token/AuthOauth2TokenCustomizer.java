package com.liuyun.auth.token;

import cn.hutool.extra.spring.SpringUtil;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.repository.user.BaseUserDetailsService;
import com.liuyun.auth.service.SysRoleService;
import com.liuyun.domain.auth.constants.AuthServerConstant;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Map;
import java.util.Set;

/**
 * AuthOauth2TokenCustomizer
 *
 * @author W.d
 * @since 2023/2/8 14:12
 **/
public class AuthOauth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

    private final SysRoleService sysRoleService;
    private final Map<String, BaseUserDetailsService> userDetailsServices;

    public AuthOauth2TokenCustomizer() {
        this.sysRoleService = SpringUtil.getBeansOfType(SysRoleService.class).entrySet().iterator().next().getValue();
        this.userDetailsServices = SpringUtil.getBeansOfType(BaseUserDetailsService.class);
    }

    /**
     * 自定义令牌荷载数据
     *
     * @param context {@link OAuth2TokenClaimsContext} 令牌信息的上下文
     * @author W.d
     * @since 2023/2/9 11:01
     **/
    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        var claims = context.getClaims();
        var clientId = context.getAuthorizationGrant().getName();
        claims.claim(AuthServerConstant.TOKEN_CLAIMS_CLIENT_ID_EXTEND, clientId);
        var grantType = context.getAuthorizationGrantType();
        // 客户端模式不返回具体用户信息
        if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(grantType)) {
            return;
        }
        var userService = Oauth2Helper.getUserDetailsServiceByGrantType(userDetailsServices, grantType.getValue());

        var user = (User) context.getPrincipal().getPrincipal();
        var username = user.getUsername();
        var userEntity = userService.getUser(username);
        var loginUser = Oauth2Helper.convertToLoginUser(userEntity);
        Set<String> roles = this.sysRoleService.queryRoleCodesByUserId(userEntity.getId());
        claims.claim(AuthServerConstant.TOKEN_CLAIMS_USER_EXTEND, loginUser);
        claims.claim(AuthServerConstant.TOKEN_CLAIMS_ROLE_EXTEND, roles);
    }
}
