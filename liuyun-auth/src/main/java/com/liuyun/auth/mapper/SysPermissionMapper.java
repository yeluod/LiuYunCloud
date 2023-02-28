package com.liuyun.auth.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.liuyun.database.mybatis.mapper.BaseCrudMapper;
import com.liuyun.domain.sys.entity.SysPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统权限表 Mapper 接口
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Mapper
@DS("liuyun-sys")
public interface SysPermissionMapper extends BaseCrudMapper<SysPermissionEntity> {

    /**
     * 根据用户ID获取菜单信息
     *
     * @param userId {@link Long} 用户ID
     * @return java.util.List<com.liuyun.domain.sys.entity.SysPermissionEntity>
     * @author W.d
     * @since 2023/2/28 17:15
     **/
    List<SysPermissionEntity> getMenusByUserId(@Param("userId") Long userId);
}
