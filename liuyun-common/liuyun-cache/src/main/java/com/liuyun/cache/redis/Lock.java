package com.liuyun.cache.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RLock;

/**
 * LockInfo
 *
 * @author W.d
 * @since 2022/9/5 17:13
 **/
@Data
@AllArgsConstructor
public class Lock {

    /**
     * 锁名称
     */
    private String lockKey;

    /**
     * 锁值
     */
    private String lockValue;

    /**
     * 获取锁超时时间
     */
    private Long waitTime;

    /**
     * 过期时间
     */
    private Long leaseTime;

    /**
     * 获取锁次数
     */
    private Integer acquireCount;

    /**
     * 锁实例
     */
    private RLock lockInstance;

}
