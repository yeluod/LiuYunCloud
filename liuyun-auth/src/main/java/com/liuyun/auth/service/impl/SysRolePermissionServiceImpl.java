package com.liuyun.auth.service.impl;

import com.liuyun.auth.mapper.SysRolePermissionMapper;
import com.liuyun.auth.service.SysRolePermissionService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysRolePermissionEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统角色权限关联表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Service
public class SysRolePermissionServiceImpl extends BaseCrudServiceImpl<SysRolePermissionMapper, SysRolePermissionEntity> implements SysRolePermissionService {

}
