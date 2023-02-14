package com.liuyun.domain.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统用户表(分页响应参数类)
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Data
@Accessors(chain = true)
@Schema(name = "系统用户表(分页响应参数类)", description = "系统用户表")
public class SysUserPageResVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "状态(ENABLE:启用; DISABLE:禁用)")
    private String status;
}
