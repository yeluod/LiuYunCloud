package com.liuyun.auth.controller;

import com.liuyun.auth.service.AuthCodeService;
import com.liuyun.base.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 *
 * @author W.d
 * @since 2023/2/27 11:45
 **/
@Slf4j
@RestController
@Tag(name = "验证码相关接口")
@RequiredArgsConstructor
@RequestMapping(value = "/code")
public class AuthCodeController {

    private final AuthCodeService authCodeService;

    @GetMapping(value = "/sms/{phone}")
    @Operation(summary = "根据手机号获取短信验证码")
    @Parameters(
            @Parameter(name = "phone", required = true, description = "手机号")
    )
    public Result<Void> sms(@PathVariable("phone") String phone) {
        this.authCodeService.getSmsCodeByPhone(phone);
        return Result.success();
    }

}
