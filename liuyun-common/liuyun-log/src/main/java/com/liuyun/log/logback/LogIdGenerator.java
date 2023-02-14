package com.liuyun.log.logback;

import cn.hutool.core.util.IdUtil;
import lombok.experimental.UtilityClass;

/**
 * LogIdGenerator
 *
 * @author W.d
 * @since 2022/12/17 16:45
 **/
@UtilityClass
public class LogIdGenerator {

    public String generateTraceId() {
        return IdUtil.fastSimpleUUID();
    }
}
