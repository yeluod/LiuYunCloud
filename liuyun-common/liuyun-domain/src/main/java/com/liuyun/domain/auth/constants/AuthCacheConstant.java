package com.liuyun.domain.auth.constants;

import lombok.experimental.UtilityClass;

/**
 * App
 *
 * @author W.d
 * @since 2022/12/17 21:03
 **/
@UtilityClass
public class AuthCacheConstant {

    /**
     * token 缓存前缀
     **/
    public final String CACHE_TOKEN_PREFIX = "auth:token";
    /**
     * token 索引缓存前缀
     **/
    public final String CACHE_TOKEN_INDEX_PREFIX = "auth:token:index";
    /**
     * token 索引 hasKey
     */
    public final String CACHE_TOKEN_INDEX_ACCESS_TOKEN = "accessToken";
    public final String CACHE_TOKEN_INDEX_REFRESH_TOKEN = "refreshToken";

    /**
     * 手机号验证码登陆缓存前缀
     */
    public final String CACHE_SMS_CODE = "auth:sms:code";

    /**
     * 客户端信息缓存 锁前缀
     */
    public final String LOCK_CLIENT_PREFIX = "lock:client";
    /**
     * 客户端信息缓存KEY前缀
     */
    public final String CACHE_CLIENT_PREFIX = "auth:client";
    /**
     * 登陆用户信息缓存前缀
     */
    public final String CACHE_LOGIN_USER_PREFIX = "auth:loginUser";
    /**
     * 登陆用户信息 hasKey 用户信息
     */
    public final String CACHE_LOGIN_USER_USER = "user";
    /**
     * 登陆用户信息 hasKey 角色信息
     */
    public final String CACHE_LOGIN_USER_ROLES = "roles";
    /**
     * 登陆用户信息 hasKey 菜单信息
     */
    public final String CACHE_LOGIN_USER_MENUS = "menus";
}
