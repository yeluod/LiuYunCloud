package com.liuyun.auth.repository.user;

import com.liuyun.domain.sys.entity.SysUserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * BaseUserDetailsService
 *
 * @author W.d
 * @since 2023/2/10 11:47
 **/
public interface BaseUserDetailsService extends UserDetailsService {

    /**
     * 获取登陆用户信息
     *
     * @param principal {@link String} 凭证
     * @return SysUserEntity
     * @author W.d
     * @since 2023/2/10 13:16
     **/
    SysUserEntity getUser(String principal);

}
