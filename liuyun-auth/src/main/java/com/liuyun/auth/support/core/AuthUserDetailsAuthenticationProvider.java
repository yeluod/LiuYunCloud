package com.liuyun.auth.support.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.auth.repository.user.BaseUserDetailsService;
import com.liuyun.auth.support.sms.PhoneSmsOauth2AuthenticationConverter;
import com.liuyun.base.utils.CacheKey;
import com.liuyun.cache.redis.RedisService;
import com.liuyun.domain.auth.constants.AuthCacheConstant;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

/**
 * AuthUserDetailsAuthenticationProvider
 *
 * @author W.d
 * @since 2023/2/8 11:48
 **/
public class AuthUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * The plaintext password used to perform
     * {@link PasswordEncoder#matches(CharSequence, String)} on when the user is not found
     * to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
     * on when the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    private final RedisService redisService;

    private final Map<String, BaseUserDetailsService> userDetailsServices;

    public AuthUserDetailsAuthenticationProvider() {
        Map<String, RedisService> redisMap = SpringUtil.getBeansOfType(RedisService.class);
        this.redisService = redisMap.entrySet().iterator().next().getValue();
        this.userDetailsServices = SpringUtil.getBeansOfType(BaseUserDetailsService.class);
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        var grantType = Oauth2Helper.getGrantTypeByRequest();
        if (PhoneSmsOauth2AuthenticationConverter.PHONE_SMS.getValue().equals(grantType)) {
            this.checkPhoneCode(authentication);
            return;
        }
        this.checkPassword(userDetails, authentication);
    }

    private void checkPassword(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        String presentedPassword = authentication.getCredentials().toString();
        Assert.isTrue(this.passwordEncoder.matches(presentedPassword, userDetails.getPassword()),
                () -> new BadCredentialsException("密码错误"));
    }

    private void checkPhoneCode(UsernamePasswordAuthenticationToken authentication) {
        var cacheKey = CacheKey.format(AuthCacheConstant.CACHE_SMS_CODE, authentication.getPrincipal());
        var code = this.redisService.get(cacheKey);
        Assert.notNull(code, () -> new BadCredentialsException("验证码已过期"));
        var presentedCode = authentication.getCredentials().toString();
        Assert.equals(presentedCode, code, () -> new BadCredentialsException("验证码错误"));
    }

    @Override
    protected void doAfterPropertiesSet() {
        Assert.isTrue(CollUtil.isNotEmpty(userDetailsServices), "A UserDetailsService must be set");
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        prepareTimingAttackProtection();
        String grantType = Oauth2Helper.getGrantTypeByRequest();
        BaseUserDetailsService userService = Oauth2Helper.getUserDetailsServiceByGrantType(userDetailsServices, grantType);
        try {
            UserDetails loadedUser = userService.loadUserByUsername(username);
            if (loadedUser == null) {
                throw new InternalAuthenticationServiceException(
                        "UserDetailsService returned null, which is an interface contract violation");
            }
            return loadedUser;
        }
        catch (UsernameNotFoundException ex) {
            mitigateAgainstTimingAttack(authentication);
            throw ex;
        }
        catch (InternalAuthenticationServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
    }

    private void prepareTimingAttackProtection() {
        if (this.userNotFoundEncodedPassword == null) {
            this.userNotFoundEncodedPassword = this.passwordEncoder.encode(USER_NOT_FOUND_PASSWORD);
        }
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    private void mitigateAgainstTimingAttack(UsernamePasswordAuthenticationToken authentication) {
        if (authentication.getCredentials() != null) {
            String presentedPassword = authentication.getCredentials().toString();
            this.passwordEncoder.matches(presentedPassword, this.userNotFoundEncodedPassword);
        }
    }
}
