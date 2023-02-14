package com.liuyun.api;

import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * BaseRemoteFallback
 *
 * @author W.d
 * @since 2022/12/17 21:35
 **/
public abstract class BaseFallback<T> implements FallbackFactory<T> {

    protected String format(String prefix, Throwable cause) {
        return prefix + ", 异常 -> " + cause.getMessage();
    }

}
