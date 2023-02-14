package com.liuyun.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 相应状态枚举
 *
 * @author W.d
 * @since 2022/7/11 17:48
 **/
@Getter
@AllArgsConstructor
public enum BaseStatusEnum implements BaseEnum, Serializable {

    /**
     * 成功
     */
    SUCCESS("00000", "success"),
    /**
     * 失败
     */
    FAIL("-1", "fail");

    /**
     * 业务编码
     */
    private final String code;
    /**
     * 业务描述
     */
    private final String message;

}
