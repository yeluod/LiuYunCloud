package com.liuyun.domain.base.page;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ApiPage
 *
 * @author W.d
 * @since 2022/11/23 16:49
 **/
@Data
public class PageReq<M> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求数据的页码
     */
    @NotNull(message = "页码不能为空")
    @Positive(message = "当前页码必须为正数")
    private Long current;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    @Positive(message = "每页条数必须为正数")
    @Max(value = 500L, message = "最大支持每页展示 500 条")
    private Long size;

    /**
     * 查询条件
     */
    @Valid
    @SuppressWarnings("all")
    private M condition;

}
