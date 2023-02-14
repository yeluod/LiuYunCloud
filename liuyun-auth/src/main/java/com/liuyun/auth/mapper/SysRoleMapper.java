package com.liuyun.auth.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.liuyun.domain.sys.entity.SysRoleEntity;
import com.liuyun.database.mybatis.mapper.BaseCrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Mapper
@DS("liuyun-sys")
public interface SysRoleMapper extends BaseCrudMapper<SysRoleEntity> {

    /**
     * 根据用户ID查询角色编码数据
     *
     * @param userId {@link Long} 用户ID
     * @return java.util.Set<java.lang.String>
     * @author W.d
     * @since 2023/2/1 13:17
     **/
    Set<String> queryRoleCodesByUserId(@Param("userId") Long userId);
}
