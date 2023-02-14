package com.liuyun.example.mapper;

import com.liuyun.database.mybatis.mapper.BaseCrudMapper;
import com.liuyun.domain.example.entity.SysTestEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统测试表 Mapper 接口
 * </p>
 *
 * @author W.d
 * @since 2023-01-31 17:17:39
 */
@Mapper
public interface SysTestMapper extends BaseCrudMapper<SysTestEntity> {

}
