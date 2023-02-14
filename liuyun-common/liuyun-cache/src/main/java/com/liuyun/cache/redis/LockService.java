package com.liuyun.cache.redis;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * LockService
 *
 * @author W.d
 * @since 2022/8/23 13:42
 **/
@Slf4j
@RequiredArgsConstructor
public class LockService {

    private final RedissonClient redissonClient;

    /**
     * 获取锁超时时间 单位：毫秒
     */
    private static final long WAIT_TIME = 3000L;
    /**
     * 过期时间 单位：毫秒
     */
    private static final long LEASE_TIME = 30000L;
    /**
     * 获取锁失败时重试时间间隔 单位：毫秒
     */
    private static final long RETRY_INTERVAL = 100L;

    public Lock lock(String key) {
        return lock(key, WAIT_TIME, LEASE_TIME);
    }

    public Lock lock(String key, long waitTime, long leaseTime) {
        return lock(key, waitTime, leaseTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 加锁方法
     *
     * @param key       锁key 同一个key只能被一个客户端持有
     * @param waitTime  尝试获取锁超时时间(ms)
     * @param leaseTime 过期时间(ms) 防止死锁
     * @return 加锁成功返回锁信息 失败返回null
     */
    public Lock lock(String key, long waitTime, long leaseTime, TimeUnit timeUnit) {
        var value = IdUtil.fastSimpleUUID();
        var start = System.currentTimeMillis();
        var count = 0;
        try {
            do {
                var acquireCount = ++count;
                var lockInstance = Optional.ofNullable(this.redissonClient.getLock(key))
                        .map(item -> {
                            try {
                                return item.tryLock(waitTime, leaseTime, timeUnit) ? item : null;
                            } catch (Exception e) {
                                log.error("lock error", e);
                                return null;
                            }
                        }).orElse(null);
                if (null != lockInstance) {
                    return new Lock(key, value, waitTime, leaseTime, acquireCount, lockInstance);
                }
                TimeUnit.MILLISECONDS.sleep(RETRY_INTERVAL);
            } while ((System.currentTimeMillis() - start) < waitTime);
        } catch (Exception e) {
            log.error("lock error", e);
            return null;
        }
        return null;
    }

    public boolean unLock(Lock lock) {
        return Optional.ofNullable(lock)
                .map(Lock::getLockInstance)
                .filter(RLock::isHeldByCurrentThread)
                .map(item -> {
                    try {
                        return item.forceUnlockAsync().get();
                    } catch (Exception e) {
                        log.error("unLock error", e);
                        return false;
                    }
                })
                .orElse(false);
    }

}
