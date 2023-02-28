package com.liuyun.auth.repository.token;

import cn.hutool.core.lang.tree.Tree;
import com.liuyun.domain.auth.dto.LoginUser;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

import java.util.List;
import java.util.Set;

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
     *
     * @param authorization {@link OAuth2Authorization}
     * @author W.d
     * @since 2023/2/13 11:58
     **/
    void deleteTokenByIndex(OAuth2Authorization authorization);

    /**
     * 根据索引ID删除token
     * <p>
     *
     * @param id {@link Object}
     * @author W.d
     * @since 2023/2/13 11:58
     **/
    void deleteTokenByIndexId(Object id);

    /**
     * 保存登陆用户信息
     *
     * @param id        {@link Long} 用户ID
     * @param loginUser {@link LoginUser} 用户信息
     * @param roles     {@link Set} 角色
     * @param menus     {@link List} 菜单
     * @author W.d
     * @since 2023/2/28 18:08
     **/
    void saveUser(Long id, LoginUser loginUser, Set<String> roles, List<Tree<Long>> menus);
}
