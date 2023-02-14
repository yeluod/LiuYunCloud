package com.liuyun.example.service.impl;

import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.example.entity.SysTestEntity;
import com.liuyun.example.mapper.SysTestMapper;
import com.liuyun.example.service.SysTestService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统测试表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-01-31 17:17:39
 */
@Service
public class SysTestServiceImpl extends BaseCrudServiceImpl<SysTestMapper, SysTestEntity> implements SysTestService {

}
