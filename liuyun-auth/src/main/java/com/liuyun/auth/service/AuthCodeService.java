package com.liuyun.auth.service;

/**
 * AuthCodeService
 *
 * @author W.d
 * @since 2023/2/27 13:13
 **/
public interface AuthCodeService {

    /**
     * 根据手机号获取短信验证码
     *
     * @param phone {@link String} 手机号
     * @author W.d
     * @since 2023/2/27 13:15
     **/
    void getSmsCodeByPhone(String phone);
}
