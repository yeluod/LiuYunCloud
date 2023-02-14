package com.liuyun.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.liuyun.domain.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 系统客户端表
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysClientEntity")
@TableName(value = "sys_client", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
public class SysClientEntity extends BaseEntity<SysClientEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端id
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 客户端名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 客户端秘钥
     */
    @TableField("client_secret")
    private String clientSecret;

    /**
     * 作用域
     */
    @TableField(value = "scopes", typeHandler = JacksonTypeHandler.class)
    private Set<String> scopes;

    /**
     * 重定向URI
     */
    @TableField(value = "redirect_uris", typeHandler = JacksonTypeHandler.class)
    private Set<String> redirectUris;

    /**
     * 授权方式
     */
    @TableField(value = "grant_types", typeHandler = JacksonTypeHandler.class)
    private Set<String> grantTypes;

    /**
     * 扩展信息 example: {"key":"value"}
     */
    @TableField(value = "additional_information", typeHandler = JacksonTypeHandler.class)
    private Map<String, String> additionalInformation;

    /**
     * access_token 过期时间(单位:秒 默认300)
     */
    @TableField("access_token_timeout")
    private Long accessTokenTimeout;

    /**
     * refresh_token 过期时间(单位:秒 默认7200)
     */
    @TableField("refresh_token_timeout")
    private Long refreshTokenTimeout;

    /**
     * 授权码过期时间(单位:秒 默认300)
     */
    @TableField("code_timeout")
    private Long codeTimeout;

    /**
     * 自动授权,授权码模式设置true跳过用户确认授权操作页面,直接跳到redirect_ur(Y: 是; N: 否)
     */
    @TableField("auto_approve")
    private String autoApprove;

    /**
     * 状态(ENABLE:启用; DISABLE:禁用)
     */
    @TableField("`status`")
    private String status;
}
