package com.liuyun.auth.controller;

import com.liuyun.auth.service.AuthOauth2Service;
import com.liuyun.base.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping(value = "/token")
public class AuthOauth2Controller {

    private final AuthOauth2Service authOauth2Service;

    @PostMapping(value = "/logout")
    @Operation(summary = "注销登录")
    public Result<Void> logout(HttpServletRequest request) {
        this.authOauth2Service.logout(request);
        return Result.success();
    }

}
