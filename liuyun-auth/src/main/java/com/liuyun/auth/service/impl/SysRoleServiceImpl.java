package com.liuyun.auth.service.impl;

import com.liuyun.auth.mapper.SysRoleMapper;
import com.liuyun.auth.service.SysRoleService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysRoleEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Service
public class SysRoleServiceImpl extends BaseCrudServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    /**
     * 根据用户ID查询角色编码数据
     *
     * @param userId {@link Long} 用户ID
     * @return java.util.Set<java.lang.String>
     * @author W.d
     * @since 2023/2/1 13:17
     **/
    @Override
    public Set<String> queryRoleCodesByUserId(Long userId) {
        return super.baseMapper.queryRoleCodesByUserId(userId);
    }
}
