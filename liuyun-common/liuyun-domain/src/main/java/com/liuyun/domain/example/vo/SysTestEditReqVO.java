package com.liuyun.domain.example.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 系统测试表(编辑请求参数类)
 *
 * @author W.d
 * @since 2023-01-09 20:42:16
 */
@Data
@Accessors(chain = true)
@Schema(name = "系统测试表(编辑请求参数类)", description = "系统测试表")
public class SysTestEditReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "生日")
    private LocalDate birthday;
}
