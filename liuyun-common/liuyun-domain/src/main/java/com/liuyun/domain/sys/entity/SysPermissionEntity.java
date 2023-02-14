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
 * 系统权限表
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysPermissionEntity")
@TableName("sys_permission")
@EqualsAndHashCode(callSuper = false)
public class SysPermissionEntity extends BaseEntity<SysPermissionEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 父级ID
     */
    @TableField("pid")
    private Long pid;

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
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 路由
     */
    @TableField("router")
    private String router;

    /**
     * 路径
     */
    @TableField("`path`")
    private String path;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 类型(MENU:菜单; BUTTON:按钮; PATH:路径)
     */
    @TableField("`type`")
    private String type;

    /**
     * 状态(ENABLE:启用; DISABLE:禁用)
     */
    @TableField("`status`")
    private String status;
}
