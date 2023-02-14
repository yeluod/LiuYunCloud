package com.liuyun.auth.service.impl;

import com.liuyun.auth.mapper.SysUserRoleMapper;
import com.liuyun.auth.service.SysUserRoleService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysUserRoleEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统用户角色关联表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Service
public class SysUserRoleServiceImpl extends BaseCrudServiceImpl<SysUserRoleMapper, SysUserRoleEntity> implements SysUserRoleService {

}
