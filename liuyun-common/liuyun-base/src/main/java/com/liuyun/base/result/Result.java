package com.liuyun.base.result;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.json.JSONUtil;
import com.liuyun.base.enums.BaseEnum;
import com.liuyun.base.enums.BaseStatusEnum;
import com.liuyun.base.exception.VerifyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Result
 * <T>
 *
 * @author W.d
 * @since 2022/11/23 14:21
 **/
@Getter
@Setter
@SuppressWarnings("unused")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务编码
     */
    private String code;
    /**
     * 业务描述
     */
    private String message;
    /**
     * 业务数据
     */
    @SuppressWarnings("all")
    private T data;

    private Result() {
    }

    public static <T> Result<T> success() {
        return success(BaseStatusEnum.SUCCESS.getCode(), BaseStatusEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return success(BaseStatusEnum.SUCCESS.getCode(), BaseStatusEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(BaseEnum baseEnum) {
        return success(baseEnum.getCode(), baseEnum.getMessage(), null);
    }

    public static <T> Result<T> success(String code, String message) {
        return success(code, message, null);
    }

    public static <T> Result<T> success(String code, String message, T data) {
        var result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail() {
        return fail(BaseStatusEnum.FAIL.getCode(), BaseStatusEnum.FAIL.getMessage());
    }

    public static <T> Result<T> fail(String message) {
        return fail(BaseStatusEnum.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(BaseEnum baseEnum) {
        return fail(baseEnum.getCode(), baseEnum.getMessage());
    }

    public static <T> Result<T> fail(String code, String message) {
        var result = new Result<T>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }



    /************************************************************************************************/

    public static <T> void verify(Result<T> result) {
        verify(result, result.getMessage());
    }

    public static <T> void verify(Result<T> result, String message) {
        verify(result, () -> new VerifyException(message));
    }

    public static <T, E extends RuntimeException> void verify(Result<T> result, Supplier<E> supplier) {
        Assert.equals(BaseStatusEnum.SUCCESS.getCode(), result.getCode(), supplier);
    }

    public static <T> T dataIgnoreNull(Result<T> result) {
        return Opt.ofNullable(result)
                .peeks(Result::verify)
                .map(Result::getData)
                .get();
    }

    public static <T> T dataNotNull(Result<T> result) {
        return dataNotNull(result, "data is null");
    }

    public static <T> T dataNotNull(Result<T> result, String message) {
        return dataNotNull(result, () -> new VerifyException(message));
    }


    public static <T, E extends RuntimeException> T dataNotNull(Result<T> result, Supplier<E> supplier) {
        return Opt.of(result)
                .peeks(Result::verify)
                .map(item -> {
                    T data = item.getData();
                    Assert.notNull(data, supplier);
                    return data;
                })
                .get();
    }

    public static <T> void write(HttpServletResponse response, Object obj) {
        JakartaServletUtil.write(response, JSONUtil.toJsonStr(obj), "application/json;charset=UTF-8");
    }
}
