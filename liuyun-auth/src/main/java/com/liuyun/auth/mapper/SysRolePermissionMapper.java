package com.liuyun.auth.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.liuyun.database.mybatis.mapper.BaseCrudMapper;
import com.liuyun.domain.sys.entity.SysRolePermissionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统角色权限关联表 Mapper 接口
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Mapper
@DS("liuyun-sys")
public interface SysRolePermissionMapper extends BaseCrudMapper<SysRolePermissionEntity> {

}
