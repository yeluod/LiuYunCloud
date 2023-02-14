package com.liuyun.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liuyun.domain.base.entity.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统角色权限关联表
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysRolePermissionEntity")
@TableName("sys_role_permission")
@EqualsAndHashCode(callSuper = false)
public class SysRolePermissionEntity extends AbstractEntity<SysRolePermissionEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 用户ID
     */
    @TableField("permission_id")
    private Long permissionId;
}
