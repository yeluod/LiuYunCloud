package com.liuyun.auth.service.impl;

import com.liuyun.auth.mapper.SysClientMapper;
import com.liuyun.auth.service.SysClientService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.sys.entity.SysClientEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统客户端表 服务实现类
 * </p>
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Service
public class SysClientServiceImpl extends BaseCrudServiceImpl<SysClientMapper, SysClientEntity> implements SysClientService {

}
