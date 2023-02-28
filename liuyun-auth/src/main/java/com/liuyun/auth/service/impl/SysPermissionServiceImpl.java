package com.liuyun.auth.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.liuyun.auth.mapper.SysPermissionMapper;
import com.liuyun.auth.service.SysPermissionService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysPermissionEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 根据用户ID获取菜单信息
     *
     * @param userId {@link Long} 用户ID
     * @return java.util.List<cn.hutool.core.lang.tree.Tree>
     * @author W.d
     * @since 2023/2/28 17:13
     **/
    @Override
    public List<Tree<Long>> loadMenusByUserId(Long userId) {
        List<SysPermissionEntity> list = super.baseMapper.getMenusByUserId(userId);
        TreeNodeConfig config = new TreeNodeConfig();
        config.setIdKey("id");
        config.setWeightKey("sort");
        config.setNameKey("name");
        config.setChildrenKey("children");
        config.setParentIdKey("pid");
        return TreeUtil.build(list, 0L, config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getPid());
            tree.setWeight(treeNode.getSort());
            tree.setName(treeNode.getName());
            // 扩展属性 ...
            tree.putExtra("icon", treeNode.getIcon());
            tree.putExtra("router", treeNode.getRouter());
        });
    }

}
