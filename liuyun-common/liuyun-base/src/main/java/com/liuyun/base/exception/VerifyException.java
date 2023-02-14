package com.liuyun.base.exception;

import com.liuyun.base.enums.BaseEnum;
import com.liuyun.base.enums.BaseStatusEnum;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 校验异常
 *
 * @author W.d
 * @since 2022/9/30 10:09
 **/
@Getter
@SuppressWarnings("unused")
public class VerifyException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    public VerifyException() {
        super();
        this.code = BaseStatusEnum.FAIL.getCode();
    }

    public VerifyException(String message) {
        super(message);
        this.code = BaseStatusEnum.FAIL.getCode();
    }

    public VerifyException(BaseEnum baseEnum) {
        super(baseEnum.getMessage());
        this.code = baseEnum.getCode();
    }

    public VerifyException(String code, String message) {
        super(message);
        this.code = code;
    }

    public VerifyException(Throwable cause) {
        super(cause);
        this.code = BaseStatusEnum.FAIL.getCode();
    }

    public VerifyException(String message, Throwable cause) {
        super(message, cause);
        this.code = BaseStatusEnum.FAIL.getCode();
    }

}
