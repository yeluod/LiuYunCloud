package com.liuyun.base.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * CacheKey
 *
 * @author W.d
 * @since 2023/2/10 13:57
 **/
@UtilityClass
public class CacheKey {

    public String format(String prefix, Object... params) {
        return prefix + ":" + Arrays.stream(params)
                .map(String::valueOf)
                .collect(Collectors.joining(":"));
    }

}
