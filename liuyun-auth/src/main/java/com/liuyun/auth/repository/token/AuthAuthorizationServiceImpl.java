package com.liuyun.auth.repository.token;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import com.liuyun.base.dto.BaseDTO;
import com.liuyun.base.utils.CacheKey;
import com.liuyun.cache.redis.RedisService;
import com.liuyun.domain.auth.constants.AuthCacheConstant;
import com.liuyun.domain.auth.constants.AuthServerConstant;
import com.liuyun.domain.auth.dto.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * AuthorizationToken
 *
 * @author W.d
 * @since 2023/2/7 11:43
 **/
@Component
@RequiredArgsConstructor
public class AuthAuthorizationServiceImpl implements AuthAuthorizationService {

    private final RedisService redisService;

    private static final Long TIMEOUT = 300L;

    private ValueOperations<String, Object> redisValueOps() {
        return this.redisService.getRedisTemplateJdkSerialize().opsForValue();
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        // 删除久的令牌信息
        this.deleteTokenByIndex(authorization);
        if (isState(authorization)) {
            String key = this.buildKey(OAuth2ParameterNames.STATE, authorization.getAttribute(OAuth2ParameterNames.STATE));
            this.redisValueOps().set(key, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
        Opt<OAuth2Authorization> opt = Opt.of(authorization);
        if (isCode(authorization)) {
            OAuth2AuthorizationCode authorizationCodeToken = opt
                    .map(item -> item.getToken(OAuth2AuthorizationCode.class))
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            long expire = this.between(authorizationCodeToken.getIssuedAt(), authorizationCodeToken.getExpiresAt());
            String key = this.buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue());
            this.redisValueOps().set(key, authorization, expire, TimeUnit.SECONDS);
        }
        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = opt.map(OAuth2Authorization::getRefreshToken)
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            long expire = this.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            String key = this.buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue());
            this.redisValueOps().set(key, authorization, expire, TimeUnit.SECONDS);
        }
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = opt.map(OAuth2Authorization::getAccessToken)
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            long expire = this.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            String key = this.buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue());
            this.redisValueOps().set(key, authorization, expire, TimeUnit.SECONDS);
        }
        // 保存令牌索引
        this.saveTokenIndex(authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        List<String> keys = new ArrayList<>(16);
        if (isState(authorization)) {
            keys.add(this.buildKey(OAuth2ParameterNames.STATE, authorization.getAttribute(OAuth2ParameterNames.STATE)));
        }
        Opt<OAuth2Authorization> opt = Opt.of(authorization);
        if (isCode(authorization)) {
            OAuth2AuthorizationCode authorizationCodeToken = opt
                    .map(item -> item.getToken(OAuth2AuthorizationCode.class))
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            keys.add(this.buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
        }
        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = opt.map(OAuth2Authorization::getRefreshToken)
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            keys.add(this.buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
        }
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = opt.map(OAuth2Authorization::getAccessToken)
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            keys.add(this.buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
        }
        this.redisService.getRedisTemplateJdkSerialize().delete(keys);
        this.deleteTokenByIndex(authorization);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");
        return Opt.ofNullable(this.buildKey(tokenType.getValue(), token))
                .map(this.redisValueOps()::get)
                .filter(OAuth2Authorization.class::isInstance)
                .map(OAuth2Authorization.class::cast)
                .filter(Objects::nonNull)
                .get();
    }

    /**
     * 保存令牌索引
     * <p>
     * * @param authorization {@link OAuth2Authorization}
     *
     * @author W.d
     * @since 2023/2/13 11:58
     **/
    @Override
    public void saveTokenIndex(OAuth2Authorization authorization) {
        if (Objects.isNull(authorization)) {
            return;
        }
        if (Objects.isNull(authorization.getAccessToken()) && Objects.isNull(authorization.getRefreshToken())) {
            return;
        }
        Long userId = getUserIdByToken(authorization);
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_TOKEN_INDEX_PREFIX, userId);
        var baseDTO = BaseDTO.create();
        long expire = 0L;
        Opt<OAuth2Authorization> opt = Opt.ofNullable(authorization);
        if (isAccessToken(authorization)) {
            OAuth2AccessToken accessToken = opt.map(OAuth2Authorization::getAccessToken)
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            var value = this.buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue());
            expire = this.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            baseDTO.set(AuthCacheConstant.CACHE_TOKEN_INDEX_ACCESS_TOKEN, value);
        }

        if (isRefreshToken(authorization)) {
            OAuth2RefreshToken refreshToken = opt.map(OAuth2Authorization::getRefreshToken)
                    .map(OAuth2Authorization.Token::getToken)
                    .orElseThrow(UnsupportedOperationException::new);
            var value = this.buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue());
            expire = this.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
            baseDTO.set(AuthCacheConstant.CACHE_TOKEN_INDEX_REFRESH_TOKEN, value);
        }
        this.redisService.hPutAll(cacheKey, baseDTO, expire, TimeUnit.SECONDS);
    }

    /**
     * 根据索引删除token
     *
     * @param authorization {@link OAuth2Authorization}
     * @author W.d
     * @since 2023/2/13 11:58
     **/
    @Override
    public void deleteTokenByIndex(OAuth2Authorization authorization) {
        Long userId = getUserIdByToken(authorization);
        String cacheKey = CacheKey.format(AuthCacheConstant.CACHE_TOKEN_INDEX_PREFIX, userId);
        Opt.ofBlankAble(cacheKey)
                .map(this.redisService::hGetAll)
                .filter(CollUtil::isNotEmpty)
                .peek(item -> this.redisService.delete(cacheKey))
                .stream()
                .flatMap(item -> item.values().stream().map(String::valueOf))
                .forEach(this.redisService::delete);
    }


    private String buildKey(String type, String id) {
        return CacheKey.format(AuthCacheConstant.CACHE_TOKEN_PREFIX, type, id);
    }

    private long between(Instant issuedAt, Instant expiresAt) {
        issuedAt = Opt.ofNullable(issuedAt).orElse(Instant.now());
        expiresAt = Opt.ofNullable(expiresAt).orElse(Instant.now());
        return ChronoUnit.SECONDS.between(issuedAt, expiresAt);
    }

    private static boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }

    private static boolean isCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);
        return Objects.nonNull(authorizationCode);
    }

    private static boolean isRefreshToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getRefreshToken());
    }

    private static boolean isAccessToken(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAccessToken());
    }

    private static Long getUserIdByToken(OAuth2Authorization authorization) {
        return Opt.ofNullable(authorization)
                .map(OAuth2Authorization::getAccessToken)
                .map(OAuth2Authorization.Token::getClaims)
                .map(item -> item.get(AuthServerConstant.TOKEN_CLAIMS_USER_EXTEND))
                .filter(Objects::nonNull)
                .map(LoginUser.class::cast)
                .map(LoginUser::getId)
                .get();
    }

}
