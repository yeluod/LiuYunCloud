package com.liuyun.domain.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 系统权限表(详情响应参数类)
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Data
@Accessors(chain = true)
@Schema(name = "系统权限表(详情响应参数类)", description = "系统权限表")
public class SysPermissionDetailResVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "父级ID")
    private Long pid;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "路由")
    private String router;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "类型(MENU:菜单; BUTTON:按钮; PATH:路径)")
    private String type;

    @Schema(description = "状态(ENABLE:启用; DISABLE:禁用)")
    private String status;
}
