package com.liuyun.auth.repository.user;

import com.liuyun.auth.service.SysUserService;
import com.liuyun.domain.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BaseUserDetailsServiceImpl
 *
 * @author W.d
 * @since 2023/2/10 12:59
 **/
public abstract class BaseUserDetailsServiceImpl implements BaseUserDetailsService {

    @Autowired
    protected SysUserService sysUserService;

    /**
     * 查询 user
     *
     * @param username {@link String} 账号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 14:25
     **/
    public SysUserEntity findByUsername(String username) {
        return this.sysUserService.loadInDatabaseByUsername(username);
    }

    /**
     * 查询 user
     *
     * @param phone {@link String} 手机号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 14:25
     **/
    public SysUserEntity findByPhone(String phone) {
        return this.sysUserService.loadInDatabaseByPhone(phone);
    }
}
