package com.liuyun.database.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 自定义 Mapper
 *
 * @author W.d
 * @since 2022/7/13 13:27
 **/
public interface BaseCrudMapper<T> extends BaseMapper<T> {

    /**
     * 批量新增
     *
     * @param coll {@link Collection<T>}
     * @return java.lang.Integer
     * @author W.d
     * @since 2022/3/25 9:27 AM
     **/
    Integer insertBatch(@Param("list") Collection<T> coll);

}
