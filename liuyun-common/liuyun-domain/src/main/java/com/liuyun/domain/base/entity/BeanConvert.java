package com.liuyun.domain.base.entity;

import cn.hutool.core.bean.BeanUtil;
import com.liuyun.base.exception.BaseException;

/**
 * BeanConvert
 *
 * @author W.d
 * @since 2022/12/7 20:18
 **/
@SuppressWarnings("unused")
public interface BeanConvert<T> {

    /**
     * 获取自动转换后的JavaBean对象
     *
     * @param clazz {@link Class<R>} 转换对象类
     * @return R {@link R} 待转换对象
     * @author W.d
     * @since 2022/7/14 09:40
     **/
    default <R> R convert(Class<R> clazz) {
        try {
            return BeanUtil.copyProperties(this, clazz);
        } catch (Exception e) {
            throw new BaseException("类型转换失败");
        }
    }

}
