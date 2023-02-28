package com.liuyun.auth.token;

import cn.hutool.core.util.IdUtil;
import com.liuyun.auth.helper.Oauth2Helper;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Base64;
import java.util.Map;

/**
 * AuthOauth2RefreshTokenGenerator
 *
 * @author W.d
 * @since 2023/2/9 11:45
 **/
public abstract class BaseOauth2TokenGenerator<T extends OAuth2Token> implements OAuth2TokenGenerator<T> {

    private final StringKeyGenerator refreshTokenGenerator =
            new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);

    protected String uuid() {
        return IdUtil.fastSimpleUUID();
    }

    protected String base64Encoded() {
        return this.refreshTokenGenerator.generateKey();
    }

    protected String jwt(Map<String, Object> payload) {
        return Oauth2Helper.generatorToken(payload);
    }

}
