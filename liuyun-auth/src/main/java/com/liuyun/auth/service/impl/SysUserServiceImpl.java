package com.liuyun.auth.service.impl;

import com.liuyun.auth.mapper.SysUserMapper;
import com.liuyun.auth.service.SysUserService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysUserEntity;
import org.springframework.stereotype.Service;

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

}
