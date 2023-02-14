package com.liuyun.auth.repository.user;

import cn.hutool.core.lang.Opt;
import com.liuyun.auth.helper.Oauth2Helper;
import com.liuyun.domain.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * DefaultUserDetailsServiceImpl
 *
 * @author W.d
 * @since 2023/2/8 13:24
 **/
@Slf4j
@Component(value = "phone_password")
public class PhonePasswordUserDetailsServiceImpl extends BaseUserDetailsServiceImpl {

    /**
     * 根据手机号查询用户信息
     *
     * @param phone {@link String} 手机号
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author W.d
     * @since 2023/2/10 10:12
     **/
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return Opt.ofNullable(super.findByPhone(phone))
                .map(Oauth2Helper::convertToUser)
                .get();
    }

    /**
     * 获取登陆用户信息
     *
     * @param principal {@link String} 凭证
     * @return SysUserEntity
     * @author W.d
     * @since 2023/2/10 13:16
     **/
    @Override
    public SysUserEntity getUser(String principal) {
        return super.findByPhone(principal);
    }

}
