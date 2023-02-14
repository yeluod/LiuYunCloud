package com.liuyun.cache;

import com.liuyun.cache.redis.RedisConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * CoreAutoConfig
 *
 * @author W.d
 * @since 2022/12/17 18:02
 **/
@Lazy
@Import(value = {
        RedisConfiguration.class
})
public class CacheAutoConfiguration {


}
