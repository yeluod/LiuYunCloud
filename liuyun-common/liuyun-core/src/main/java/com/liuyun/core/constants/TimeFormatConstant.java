package com.liuyun.core.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

/**
 * TimeFormatConstant
 *
 * @author W.d
 * @since 2022/8/12 11:42
 **/
@UtilityClass
public class TimeFormatConstant {

    public final DateTimeFormatter DEFAULT_HMS = DateTimeFormatter.ofPattern("HH:mm:ss");
    public final DateTimeFormatter DEFAULT_YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public final DateTimeFormatter DEFAULT_YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

}
