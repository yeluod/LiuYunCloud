package com.liuyun.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liuyun.domain.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统角色表
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysRoleEntity")
@TableName("sys_role")
@EqualsAndHashCode(callSuper = false)
public class SysRoleEntity extends BaseEntity<SysRoleEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 编码
     */
    @TableField("`code`")
    private String code;

    /**
     * 状态(ENABLE:启用; DISABLE:禁用)
     */
    @TableField("`status`")
    private String status;
}
