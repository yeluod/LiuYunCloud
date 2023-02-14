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

    public final String CACHE_TOKEN_PREFIX = "auth:token";
    public final String CACHE_TOKEN_INDEX_PREFIX = "auth:token:index";
    public final String CACHE_TOKEN_INDEX_ACCESS_TOKEN = "accessToken";
    public final String CACHE_TOKEN_INDEX_REFRESH_TOKEN = "refreshToken";


    public final String CACHE_CLIENT_PREFIX = "auth:client";
    public final String CACHE_SMS_CODE = "auth:sms:code";
    public final String CACHE_USER_USERNAME_PREFIX = "auth:user:username";
    public final String CACHE_USER_PHONE_PREFIX = "auth:user:phone";


    public final String LOCK_CLIENT_PREFIX = "lock:client";
    public final String LOCK_USER_USERNAME_PREFIX = "lock:user:username";
    public final String LOCK_USER_PHONE_PREFIX = "lock:user:phone";
}
