package com.liuyun.domain.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统客户端表(分页响应参数类)
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Data
@Accessors(chain = true)
@Schema(name = "系统客户端表(分页响应参数类)", description = "系统客户端表")
public class SysClientPageResVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "客户端id")
    private String clientId;

    @Schema(description = "客户端名称")
    private String clientName;

    @Schema(description = "客户端秘钥")
    private String clientSecret;

    @Schema(description = "作用域")
    private String scopes;

    @Schema(description = "重定向URI")
    private String redirectUris;

    @Schema(description = "授权方式")
    private String grantTypes;

    @Schema(description = "扩展信息 example: {\"key\":\"value\"}")
    private String additionalInformation;

    @Schema(description = "access_token 过期时间(单位:秒 默认300)")
    private Long accessTokenTimeout;

    @Schema(description = "refresh_token 过期时间(单位:秒 默认7200)")
    private Long refreshTokenTimeout;

    @Schema(description = "授权码过期时间(单位:秒 默认300)")
    private Long codeTimeout;

    @Schema(description = "是否在每次 Refresh-Token 刷新 Access-Token 时，产生一个新的 Refresh-Token")
    private Boolean newRefreshToken;

    @Schema(description = "自动授权,授权码模式设置true跳过用户确认授权操作页面,直接跳到redirect_ur(Y: 是; N: 否)")
    private String autoApprove;

    @Schema(description = "状态(ENABLE:启用; DISABLE:禁用)")
    private String status;
}
