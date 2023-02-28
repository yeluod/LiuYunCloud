package com.liuyun.auth.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.liuyun.auth.mapper.SysUserMapper;
import com.liuyun.auth.service.SysUserService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysUserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Service
public class SysUserServiceImpl extends BaseCrudServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {

    /**
     * 从数据库中加载用户信息
     *
     * @param username {@link String} 账号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 12:58
     **/
    @Override
    public SysUserEntity loadInDatabaseByUsername(String username) {
        return new LambdaQueryChainWrapper<>(super.baseMapper)
                .eq(SysUserEntity::getUsername, username)
                .oneOpt()
                .filter(entity -> Objects.nonNull(entity.getId()))
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }

    /**
     * 从数据库中加载用户信息
     *
     * @param phone {@link String} 手机号
     * @return com.liuyun.domain.sys.entity.SysUserEntity
     * @author W.d
     * @since 2023/2/10 12:58
     **/
    @Override
    public SysUserEntity loadInDatabaseByPhone(String phone) {
        return new LambdaQueryChainWrapper<>(super.baseMapper)
                .eq(SysUserEntity::getPhone, phone)
                .oneOpt()
                .filter(entity -> Objects.nonNull(entity.getId()))
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }

}
