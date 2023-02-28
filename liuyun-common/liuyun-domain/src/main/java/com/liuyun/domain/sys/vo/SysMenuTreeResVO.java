package com.liuyun.domain.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * SysMenuTreeResVO
 *
 * @author W.d
 * @since 2023/2/28 16:55
 **/
@Data
@Accessors(chain = true)
@Schema(name = "系统菜单(菜单树)", description = "系统菜单")
public class SysMenuTreeResVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "路由")
    private String router;

    @Schema(description = "排序")
    private Integer sort;
}
