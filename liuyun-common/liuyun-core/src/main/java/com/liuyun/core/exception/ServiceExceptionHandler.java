package com.liuyun.core.exception;

import com.liuyun.base.enums.BaseStatusEnum;
import com.liuyun.base.exception.VerifyException;
import com.liuyun.base.result.Result;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedCheckedException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ServiceExceptionHandler
 *
 * @author W.d
 * @since 2022/7/14 13:03
 **/
@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class ServiceExceptionHandler {

    /**
     * 验证异常处理 - 在 @RequestBody 上添加 @Validated 处触发
     *
     * @param e       {@link MethodArgumentNotValidException}
     * @return com.ly.core.api.Result<java.lang.Object>
     * @author W.d
     * @since 2022/3/26 1:46 PM
     **/
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.fail(BaseStatusEnum.FAIL.getCode(), this.convertFiledErrors(e.getBindingResult().getFieldErrors()));
    }

    /**
     * 验证异常处理 - @Validated加在 controller 类上，
     * 且在参数列表中直接指定constraints时触发
     *
     * @param ex      {@link ConstraintViolationException}
     * @return com.ly.core.api.Result<java.lang.Object>
     * @author W.d
     * @since 2022/4/6 2:45 PM
     **/
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({ConstraintViolationException.class})
    public Result<String> handleConstraintViolationException(ConstraintViolationException ex) {
        return Result.fail(BaseStatusEnum.FAIL.getCode(), this.convertConstraintViolations(ex));
    }

    /**
     * 验证异常处理 - form参数（对象参数，没有加 @RequestBody）触发
     *
     * @param e       {@link BindException}
     * @return com.ly.core.api.Result<java.lang.Object>
     * @author W.d
     * @since 2022/3/26 1:46 PM
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BindException.class})
    public Result<String> handleBindException(BindException e) {
        return Result.fail(BaseStatusEnum.FAIL.getCode(), this.convertFiledErrors(e.getBindingResult().getFieldErrors()));
    }

    /**
     * 转换FieldError列表为错误提示信息
     *
     * @param fieldErrors {@link List<FieldError>} fieldErrors
     * @return java.lang.String
     * @author W.d
     * @since 2022/3/26 1:43 PM
     **/
    private String convertFiledErrors(List<FieldError> fieldErrors) {
        return Optional.ofNullable(fieldErrors)
                .map(fieldErrorsInner -> fieldErrorsInner.stream()
                        .flatMap(fieldError -> Stream.of(fieldError.getField() + " " + fieldError.getDefaultMessage()))
                        .collect(Collectors.joining(", ")))
                .orElse(null);
    }

    /**
     * 转换ConstraintViolationException 异常为错误提示信息
     *
     * @param e {@link ConstraintViolationException} Exception
     * @return java.lang.String
     * @author W.d
     * @since 2022/3/26 1:42 PM
     **/
    private String convertConstraintViolations(ConstraintViolationException e) {
        return Optional.ofNullable(e.getConstraintViolations())
                .map(constraintViolations -> constraintViolations.stream()
                        .flatMap(constraintViolation -> {
                            var path = constraintViolation.getPropertyPath().toString();
                            var errorMessage = path.substring(path.lastIndexOf(".") + 1) +
                                               " " + constraintViolation.getMessage();
                            return Stream.of(errorMessage);
                        }).collect(Collectors.joining(", "))
                ).orElse(null);
    }

    /**
     * 自定义 REST 业务异常
     *
     * @param e    {@link Throwable}
     * @param resp {@link HttpServletResponse}
     * @return com.ly.core.api.Result<java.lang.Object>
     * @author W.d
     * @since 2022/3/26 1:48 PM
     **/
    @ExceptionHandler(value = Throwable.class)
    public Result<String> handleBadRequest(Throwable e, HttpServletResponse resp) {
        // 业务逻辑异常
        if (e instanceof VerifyException be) {
            return Result.fail(be.getCode(), be.getMessage());
        }
        // IllegalArgumentException
        if (e instanceof IllegalArgumentException iae) {
            return Result.fail(BaseStatusEnum.FAIL.getCode(), iae.getMessage());
        }
        // 参数缺失
        if (e instanceof NestedCheckedException nce) {
            return Result.fail(BaseStatusEnum.FAIL.getCode(), nce.getMessage());
        }
        if (e instanceof NestedRuntimeException nre) {
            return Result.fail(BaseStatusEnum.FAIL.getCode(), nre.getMessage());
        }
        // 系统内部异常，打印异常栈
        log.error("系统内部异常", e);
        return Result.fail(BaseStatusEnum.FAIL.getCode(), "系统内部异常, 请联系管理员");
    }

}
