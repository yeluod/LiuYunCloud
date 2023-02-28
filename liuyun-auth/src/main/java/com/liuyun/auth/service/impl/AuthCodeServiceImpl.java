package com.liuyun.auth.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.liuyun.auth.service.AuthCodeService;
import com.liuyun.auth.service.SysUserService;
import com.liuyun.base.exception.VerifyException;
import com.liuyun.base.utils.CacheKey;
import com.liuyun.cache.redis.RedisService;
import com.liuyun.domain.auth.constants.AuthCacheConstant;
import com.liuyun.domain.base.enums.BaseEnableEnum;
import com.liuyun.domain.sys.entity.SysUserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * AuthCodeServiceImpl
 *
 * @author W.d
 * @since 2023/2/27 13:13
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCodeServiceImpl implements AuthCodeService {

    private final RedisService redisService;
    private final SysUserService sysUserService;

    /**
     * 根据手机号获取短信验证码
     *
     * @param phone {@link String} 手机号
     * @author W.d
     * @since 2023/2/27 13:15
     **/
    @Override
    public void getSmsCodeByPhone(String phone) {
        SysUserEntity entity = this.sysUserService.loadInDatabaseByPhone(phone);
        Assert.isTrue(StrUtil.equals(BaseEnableEnum.ENABLE.name(), entity.getStatus()),
                () -> new VerifyException("该用户已禁用!!!"));
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_SMS_CODE, phone);
        var oldCode = this.redisService.get(cacheKey);
        Assert.isNull(oldCode, () -> new VerifyException("验证码尚在有效期内!!!"));
        var newCode = StrUtil.toString(RandomUtil.randomInt(100000, 999999));
        this.redisService.set(cacheKey, newCode, 5L, TimeUnit.MINUTES);
        log.info("newCode is: -> [{}]", newCode);
    }
}
