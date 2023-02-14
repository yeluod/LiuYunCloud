package com.liuyun.auth.repository.token;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * AuthAuthorizationService
 *
 * @author W.d
 * @since 2023/2/13 11:38
 **/
public interface AuthAuthorizationService extends OAuth2AuthorizationService {

    /**
     * 保存令牌索引
     * <p>
     * * @param authorization {@link OAuth2Authorization}
     *
     * @param authorization {@link OAuth2Authorization} 授权信息
     * @author W.d
     * @since 2023/2/13 11:58
     **/
    void saveTokenIndex(OAuth2Authorization authorization);

    /**
     * 根据索引删除token
     * <p>
     * * @param authorization {@link OAuth2Authorization}
     *
     * @author W.d
     * @since 2023/2/13 11:58
     **/
    void deleteTokenByIndex(OAuth2Authorization authorization);
}
