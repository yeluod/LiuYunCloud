package com.liuyun.auth.service.impl;

import com.liuyun.auth.mapper.SysPermissionMapper;
import com.liuyun.auth.service.SysPermissionService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysPermissionEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统权限表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Service
public class SysPermissionServiceImpl extends BaseCrudServiceImpl<SysPermissionMapper, SysPermissionEntity> implements SysPermissionService {

}
