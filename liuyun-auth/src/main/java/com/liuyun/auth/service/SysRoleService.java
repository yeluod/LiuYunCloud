package com.liuyun.auth.service;

import com.liuyun.database.mybatis.service.BaseCrudService;
import com.liuyun.domain.sys.entity.SysRoleEntity;

import java.util.Set;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
public interface SysRoleService extends BaseCrudService<SysRoleEntity> {

    /**
     * 根据用户ID查询角色编码数据
     *
     * @param userId {@link Long} 用户ID
     * @return java.util.Set<java.lang.String>
     * @author W.d
     * @since 2023/2/1 13:17
     **/
    Set<String> queryRoleCodesByUserId(Long userId);

}
