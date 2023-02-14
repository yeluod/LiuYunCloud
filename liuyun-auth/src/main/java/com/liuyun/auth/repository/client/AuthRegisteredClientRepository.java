package com.liuyun.auth.repository.client;

import cn.hutool.core.lang.Opt;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.service.SysClientService;
import com.liuyun.base.utils.CacheKey;
import com.liuyun.cache.redis.LockService;
import com.liuyun.cache.redis.RedisService;
import com.liuyun.domain.auth.constants.AuthCacheConstant;
import com.liuyun.domain.base.enums.BaseEnableEnum;
import com.liuyun.domain.sys.entity.SysClientEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * AuthRegisteredClientRepository
 *
 * @author W.d
 * @since 2023/2/7 09:45
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthRegisteredClientRepository implements RegisteredClientRepository {

    private final LockService lockService;
    private final RedisService redisService;
    private final SysClientService sysClientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        // 第一次查询缓存
        var entity = this.loadInCache(clientId);
        if (Objects.nonNull(entity)) {
            return Oauth2Helper.convertToClient(entity);
        }
        var lockKey = CacheKey.format(AuthCacheConstant.LOCK_CLIENT_PREFIX, clientId);
        var lock = this.lockService.lock(lockKey);
        try {
            if (Objects.nonNull(lock)) {
                // 第二次查询缓存
                entity = this.loadInCache(clientId);
                if (Objects.nonNull(entity)) {
                    return Oauth2Helper.convertToClient(entity);
                }
                // 查询数据库
                entity = this.loadInDatabase(clientId);
                if (Objects.nonNull(entity)) {
                    // 放入缓存
                    this.saveToCache(entity);
                    return Oauth2Helper.convertToClient(entity);
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
     * 从数据库中加载客户端信息
     *
     * @param clientId {@link String} clientId
     * @return com.liuyun.domain.sys.entity.SysClientEntity
     * @author W.d
     * @since 2023/2/9 17:38
     **/
    private SysClientEntity loadInDatabase(String clientId) {
        return new LambdaQueryChainWrapper<>(this.sysClientService.getBaseMapper())
                .eq(SysClientEntity::getClientId, clientId)
                .eq(SysClientEntity::getStatus, BaseEnableEnum.ENABLE.name())
                .one();
    }

    /**
     * 从缓存中加载客户端信息
     *
     * @param clientId {@link String} clientId
     * @return com.liuyun.domain.sys.entity.SysClientEntity
     * @author W.d
     * @since 2023/2/9 17:49
     **/
    private SysClientEntity loadInCache(String clientId) {
        return Opt.ofNullable(this.redisService.hGet(AuthCacheConstant.CACHE_CLIENT_PREFIX, clientId))
                .filter(Objects::nonNull)
                .filter(SysClientEntity.class::isInstance)
                .map(SysClientEntity.class::cast)
                .get();
    }

    /**
     * 保存客户端信息至缓存
     *
     * @param entity {@link SysClientEntity} 客户端信息
     * @author W.d
     * @since 2023/2/9 17:49
     **/
    private void saveToCache(SysClientEntity entity) {
        this.redisService.hPut(AuthCacheConstant.CACHE_CLIENT_PREFIX, entity.getClientId(), entity);
    }

}
