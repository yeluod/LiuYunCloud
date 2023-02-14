package com.liuyun.log.logback;

import ch.qos.logback.classic.PatternLayout;

/**
 * BhPatternLayout
 *
 * @author W.d
 * @since 2022/10/20 14:50
 **/
public class BhPatternLayout extends PatternLayout {

    static {
        DEFAULT_CONVERTER_MAP.put("hostAddress", IpConverter.class.getName());
    }

}
