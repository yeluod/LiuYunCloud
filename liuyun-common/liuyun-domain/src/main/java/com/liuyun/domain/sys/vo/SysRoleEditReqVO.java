package com.liuyun.domain.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统角色表(编辑请求参数类)
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Data
@Accessors(chain = true)
@Schema(name = "系统角色表(编辑请求参数类)", description = "系统角色表")
public class SysRoleEditReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "状态(ENABLE:启用; DISABLE:禁用)")
    private String status;
}
