package com.liuyun.base.dto;

import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * BaseBean
 *
 * @author W.d
 * @since 2022/8/2 17:32
 **/
@ToString
public final class BaseDTO extends LinkedHashMap<String, Object> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BaseDTO() {
    }

    public static BaseDTO create() {
        return new BaseDTO();
    }

    public BaseDTO set(String key, Object value) {
        this.put(key, value);
        return this;
    }

}
