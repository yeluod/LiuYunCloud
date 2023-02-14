package com.liuyun.cache.redis;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * RedisConfiguration
 *
 * @author W.d
 * @since 2022/8/17 15:32
 **/
@Lazy
@EnableCaching
@Import(value = {
        RedisService.class,
        LockService.class
})
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RedisConfiguration {

    public static final String REDIS_TEMPLATE_BEAN_NAME = "redisTemplate";
    public static final String REDIS_TEMPLATE_BEAN_NAME_JDK = "redisTemplateJdkSerialize";

    private final RedisProperties redisProperties;

    /**
     * 自定义key生成器
     *
     * @return org.springframework.cache.interceptor.KeyGenerator
     * @author W.d
     * @since 2022/8/23 09:19
     **/
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            var sb = new StringBuilder();
            // 类目
            sb.append(target.getClass().getName());
            // 方法名
            sb.append(method.getName());
            for (Object param : params) {
                // 参数名
                sb.append(param.toString());
            }
            return sb.toString();
        };
    }

    /**
     * LettuceConnectionFactory
     *
     * @return org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
     * @author W.d
     * @since 2022/8/23 13:30
     **/
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        var redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        var lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .build();
        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }


    /**
     * 配置 RedisTemplate
     *
     * @param lettuceConnectionFactory {@link }
     * @return org.springframework.data.redis.core.RedisTemplate<java.lang.String, java.lang.Object>
     * @author W.d
     * @since 2022/8/23 09:20
     **/
    @Bean(name = REDIS_TEMPLATE_BEAN_NAME)
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return Opt.ofNullable(lettuceConnectionFactory)
                .map(item -> {
                    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
                    redisTemplate.setConnectionFactory(item);
                    return redisTemplate;
                })
                .peeks(
                        item -> item.setDefaultSerializer(this.stringRedisSerializer()),
                        item -> item.setKeySerializer(this.stringRedisSerializer()),
                        item -> item.setHashKeySerializer(this.stringRedisSerializer()),
                        item -> item.setValueSerializer(this.genericJackson2JsonRedisSerializer()),
                        item -> item.setHashValueSerializer(this.genericJackson2JsonRedisSerializer()),
                        RedisTemplate::afterPropertiesSet
                )
                .orElseThrow(() -> new RuntimeException("redisTemplate init error"));
    }

    @Bean(name = REDIS_TEMPLATE_BEAN_NAME_JDK)
    public RedisTemplate<String, Object> redisTemplateJdkSerialize(LettuceConnectionFactory lettuceConnectionFactory) {
        return Opt.ofNullable(lettuceConnectionFactory)
                .map(item -> {
                    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
                    redisTemplate.setConnectionFactory(item);
                    return redisTemplate;
                })
                .peeks(
                        item -> item.setDefaultSerializer(this.stringRedisSerializer()),
                        item -> item.setKeySerializer(this.stringRedisSerializer()),
                        item -> item.setHashKeySerializer(this.stringRedisSerializer()),
                        item -> item.setValueSerializer(RedisSerializer.java()),
                        item -> item.setHashValueSerializer(RedisSerializer.java()),
                        RedisTemplate::afterPropertiesSet
                )
                .orElseThrow(() -> new RuntimeException("redisTemplate init error"));
    }


    /**
     * StringRedisSerializer
     *
     * @return org.springframework.data.redis.serializer.RedisSerializer<java.lang.String>
     * @author W.d
     * @since 2022/8/23 09:22
     **/
    private RedisSerializer<String> stringRedisSerializer() {
        return RedisSerializer.string();
    }

    /**
     * GenericJackson2JsonRedisSerializer
     *
     * @return org.springframework.data.redis.serializer.RedisSerializer<java.lang.String>
     * @author W.d
     * @since 2022/8/23 09:22
     **/
    private RedisSerializer<Object> genericJackson2JsonRedisSerializer() {
        return Opt.ofNullable(new ObjectMapper())
                .peeks(
                        item -> item.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY),
                        item -> item.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS),
                        // 解决jackson2无法反序列化LocalDateTime的问题
                        item -> item.registerModule(new JavaTimeModule()),
                        item -> item.activateDefaultTyping(item.getPolymorphicTypeValidator(),
                                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)

                )
                // value 序列化方式 GenericJackson2JsonRedisSerializer
                .map(GenericJackson2JsonRedisSerializer::new)
                .orElseThrow(() -> new RuntimeException("genericJackson2JsonRedisSerializer init error"));
    }

    @Bean
    public RedissonClient redissonClient() {
        var config = new Config();
        var redisUrl = StrUtil.format("redis://{}:{}", redisProperties.getHost(),
                redisProperties.getPort());
        config.useSingleServer()
                .setAddress(redisUrl)
                .setDatabase(0)
                .setPassword(redisProperties.getPassword());
        return Redisson.create(config);
    }
}
