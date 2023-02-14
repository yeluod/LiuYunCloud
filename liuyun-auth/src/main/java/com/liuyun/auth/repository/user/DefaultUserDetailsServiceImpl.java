package com.liuyun.auth.repository.user;

import cn.hutool.core.lang.Opt;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.domain.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * DefaultUserDetailsServiceImpl
 *
 * @author W.d
 * @since 2023/2/8 13:24
 **/
@Slf4j
@Component(value = "default")
public class DefaultUserDetailsServiceImpl extends BaseUserDetailsServiceImpl {

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     * GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Opt.ofNullable(super.findByUsername(username))
                .map(Oauth2Helper::convertToUser)
                .get();
    }

    /**
     * 获取登陆用户信息
     *
     * @param principal {@link String} 凭证
     * @return SysUserEntity
     * @author W.d
     * @since 2023/2/10 13:16
     **/
    @Override
    public SysUserEntity getUser(String principal) {
        return super.findByUsername(principal);
    }
}
