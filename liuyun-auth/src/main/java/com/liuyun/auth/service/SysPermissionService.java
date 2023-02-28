package com.liuyun.auth.service;

import cn.hutool.core.lang.tree.Tree;
import com.liuyun.database.mybatis.service.BaseCrudService;
import com.liuyun.domain.sys.entity.SysPermissionEntity;

import java.util.List;

/**
 * <p>
 * 系统权限表 服务类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
public interface SysPermissionService extends BaseCrudService<SysPermissionEntity> {

    /**
     * 根据用户ID获取菜单信息
     *
     * @param userId {@link Long} 用户ID
     * @return java.util.List<cn.hutool.core.lang.tree.Tree>
     * @author W.d
     * @since 2023/2/28 17:13
     **/
    List<Tree<Long>> loadMenusByUserId(Long userId);

}
