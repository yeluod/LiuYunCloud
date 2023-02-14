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
 * 系统用户表
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysUserEntity")
@TableName("sys_user")
@EqualsAndHashCode(callSuper = false)
public class SysUserEntity extends BaseEntity<SysUserEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 账号
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 性别
     */
    @TableField("gender")
    private String gender;

    /**
     * 状态(ENABLE:启用; DISABLE:禁用)
     */
    @TableField("`status`")
    private String status;
}
