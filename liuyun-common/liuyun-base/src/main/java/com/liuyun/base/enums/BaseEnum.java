package com.liuyun.base.enums;

import java.io.Serializable;

/**
 * BaseEnum
 *
 * @author W.d
 * @since 2022/7/11 17:40
 **/
public interface BaseEnum extends Serializable {

    /**
     * 业务状态码
     *
     * @return java.lang.String
     * @author W.d
     * @since 2022/7/11 17:41
     **/
    String getCode();

    /**
     * 业务描述
     *
     * @return java.lang.String
     * @author W.d
     * @since 2022/7/11 17:41
     **/
    String getMessage();

}
