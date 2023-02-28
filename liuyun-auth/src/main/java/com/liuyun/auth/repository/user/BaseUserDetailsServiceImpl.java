package com.liuyun.auth.repository.user;

import cn.hutool.core.lang.Opt;
import com.liuyun.auth.service.SysUserService;
import com.liuyun.base.utils.CacheKey;
import com.liuyun.cache.redis.LockService;
import com.liuyun.cache.redis.RedisService;
import com.liuyun.domain.auth.constants.AuthCacheConstant;
import com.liuyun.domain.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * BaseUserDetailsServiceImpl
 *
 * @author W.d
 * @since 2023/2/10 12:59
 **/
public abstract class BaseUserDetailsServiceImpl implements BaseUserDetailsService {

    @Autowired
    protected LockService lockService;
    @Autowired
    protected RedisService redisService;
    @Autowired
    protected SysUserService sysUserService;

    /**
     * 从缓存中加载用户信息
     *
     * @param username {@link String} 账号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 12:58
     **/
    public SysUserEntity loadInCacheByUsername(String username) {
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_USER_USERNAME_PREFIX, username);
        return Opt.ofNullable(this.redisService.get(cacheKey)).map(SysUserEntity.class::cast).get();
    }

    /**
     * 从缓存中加载用户信息
     *
     * @param phone {@link String} 手机号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 12:58
     **/
    public SysUserEntity loadInCacheByPhone(String phone) {
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_USER_PHONE_PREFIX, phone);
        return Opt.ofNullable(this.redisService.get(cacheKey)).map(SysUserEntity.class::cast).get();
    }

    /**
     * 保存客户信息至缓存
     *
     * @param entity {@link SysUserEntity} 用户信息
     * @author W.d
     * @since 2023/2/9 17:49
     **/
    private void saveToCacheByUsername(String suffix, SysUserEntity entity) {
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_USER_USERNAME_PREFIX, suffix);
        this.redisService.set(cacheKey, entity);
    }

    /**
     * 保存客户信息至缓存
     *
     * @param entity {@link SysUserEntity} 用户信息
     * @author W.d
     * @since 2023/2/9 17:49
     **/
    private void saveToCacheByPhone(String suffix, SysUserEntity entity) {
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_USER_PHONE_PREFIX, suffix);
        this.redisService.set(cacheKey, entity);
    }

    /**
     * 查询 user
     *
     * @param username {@link String} 账号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 14:25
     **/
    public SysUserEntity findByUsername(String username) {
        // 第一次查询缓存
        var entity = this.loadInCacheByUsername(username);
        if (Objects.nonNull(entity)) {
            return entity;
        }
        var lockKey = CacheKey.format(AuthCacheConstant.LOCK_USER_USERNAME_PREFIX, username);
        var lock = this.lockService.lock(lockKey);
        try {
            if (Objects.nonNull(lock)) {
                // 第二次查询缓存
                entity = this.loadInCacheByUsername(username);
                if (Objects.nonNull(entity)) {
                    return entity;
                }
                // 查询数据库
                entity = this.sysUserService.loadInDatabaseByUsername(username);
                if (Objects.nonNull(entity)) {
                    // 放入缓存
                    this.saveToCacheByUsername(username, entity);
                    return entity;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            this.lockService.unLock(lock);
        }
    }

    /**
     * 查询 user
     *
     * @param phone {@link String} 手机号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 14:25
     **/
    public SysUserEntity findByPhone(String phone) {
        // 第一次查询缓存
        var entity = this.loadInCacheByPhone(phone);
        if (Objects.nonNull(entity)) {
            return entity;
        }
        var lockKey = CacheKey.format(AuthCacheConstant.LOCK_USER_PHONE_PREFIX, phone);
        var lock = this.lockService.lock(lockKey);
        try {
            if (Objects.nonNull(lock)) {
                // 第二次查询缓存
                entity = this.loadInCacheByPhone(phone);
                if (Objects.nonNull(entity)) {
                    return entity;
                }
                // 查询数据库
                entity = this.sysUserService.loadInDatabaseByPhone(phone);
                if (Objects.nonNull(entity)) {
                    // 放入缓存
                    this.saveToCacheByPhone(phone, entity);
                    return entity;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            this.lockService.unLock(lock);
        }
    }
}
