package com.liuyun.cache.redis;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RedisUtil
 *
 * @author W.d
 * @since 2022/8/18 11:42
 **/
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisTemplateJdkSerialize;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    public RedisTemplate<String, Object> getRedisTemplateJdkSerialize() {
        return this.redisTemplateJdkSerialize;
    }

    public ValueOperations<String, Object> valueOps() {
        return redisTemplate.opsForValue();
    }

    public ListOperations<String, Object> listOps() {
        return redisTemplate.opsForList();
    }

    public SetOperations<String, Object> setOps() {
        return redisTemplate.opsForSet();
    }

    public ZSetOperations<String, Object> zSetOps() {
        return redisTemplate.opsForZSet();
    }

    public HashOperations<String, String, Object> hashOps() {
        return redisTemplate.opsForHash();
    }

    public void expire(String key, long timeout, TimeUnit unit) {
        checkKey(key);
        redisTemplate.expire(key, timeout, unit);
    }

    public void delete(String... keys) {
        delete(Stream.of(keys).collect(Collectors.toSet()));
    }

    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    public void set(String key, Object value) {
        checkKey(key);
        valueOps().set(key, value);
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        checkKey(key);
        valueOps().set(key, value, timeout, unit);
    }

    public Object get(String key) {
        checkKey(key);
        return valueOps().get(key);
    }

    public void hPut(String key, String hashKey, Object value) {
        checkKey(key);
        checkKey(hashKey);
        hashOps().put(key, hashKey, value);
    }

    public void hPut(String key, String hashKey, Object value, long timeout, TimeUnit unit) {
        checkKey(key);
        checkKey(hashKey);
        hashOps().put(key, hashKey, value);
        expire(key, timeout, unit);
    }

    public void hPutAll(String key, Map<String, Object> map) {
        checkKey(key);
        hashOps().putAll(key, map);
    }

    public void hPutAll(String key, Map<String, Object> map, long timeout, TimeUnit unit) {
        checkKey(key);
        hashOps().putAll(key, map);
        expire(key, timeout, unit);
    }

    public Object hGet(String key, String hashKey) {
        checkKey(key);
        checkKey(hashKey);
        return hashOps().get(key, hashKey);
    }

    public Map<String, Object> hGetAll(String key) {
        checkKey(key);
        return hashOps().entries(key);
    }

    public Long hDel(String key, Object... hashKeys) {
        checkKey(key);
        return Optional.of(hashOps().delete(key, hashKeys)).orElse(0L);
    }

    public void checkKey(Object key) {
        if (key instanceof CharSequence strKey) {
            Assert.notBlank(strKey, "key must be not blank !");
        } else {
            Assert.notNull(key, "key must be not blank !");
        }
    }
}
