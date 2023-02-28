package com.liuyun.auth.service;

import com.liuyun.database.mybatis.service.BaseCrudService;
import com.liuyun.domain.sys.entity.SysUserEntity;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
public interface SysUserService extends BaseCrudService<SysUserEntity> {

    /**
     * 从数据库中加载用户信息
     *
     * @param username {@link String} 账号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 12:58
     **/
    SysUserEntity loadInDatabaseByUsername(String username);

    /**
     * 从数据库中加载用户信息
     *
     * @param phone {@link String} 手机号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 12:58
     **/
    SysUserEntity loadInDatabaseByPhone(String phone);

}
