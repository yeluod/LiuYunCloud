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
 * 系统用户角色关联表
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysUserRoleEntity")
@TableName("sys_user_role")
@EqualsAndHashCode(callSuper = false)
public class SysUserRoleEntity extends AbstractEntity<SysUserRoleEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;
}
