package com.liuyun.database.mybatis.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liuyun.base.exception.VerifyException;
import com.liuyun.domain.base.page.PageReq;
import com.liuyun.domain.base.page.PageRes;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * 自定义 Service
 *
 * @author W.d
 * @since 2022/7/13 13:26
 **/
public interface BaseCrudService<T> extends IService<T> {

    /**
     * 根据 ID 查询 检查数据合法性
     *
     * @param id {@link Long} id
     * @return T
     * @author W.d
     * @since 2022/3/25 5:35 PM
     **/
    default T checkById(Long id) {
        T t = this.getById(id);
        Assert.notNull(t, () -> new VerifyException("指定ID查询数据不存在"));
        return t;
    }

    /**
     * 校验指定条件是否存在
     *
     * @param condition {@link Boolean} 判断条件
     * @param supplier  {@link Supplier} 查询条件
     * @param message   {@link String} 提示信息
     * @author W.d
     * @since 2022/3/25 5:36 PM
     **/
    default void checkExists(boolean condition, Supplier<LambdaQueryWrapper<T>> supplier, String message) {
        if (condition) {
            this.checkExists(supplier.get(), message);
        }
    }

    /**
     * 校验指定条件是否存在
     *
     * @param lqw     {@link LambdaQueryWrapper} 查询条件 LambdaQueryWrapper
     * @param message {@link String} 提示信息
     * @author W.d
     * @since 2022/3/25 5:36 PM
     **/
    default void checkExists(LambdaQueryWrapper<T> lqw, String message) {
        Assert.notEmpty(this.list(lqw), () -> new VerifyException(message));
    }

    /**
     * 批量新增
     *
     * @param coll {@link Collection<T>}
     * @return java.lang.Integer
     * @author W.d
     * @since 2022/3/25 9:27 AM
     **/
    @Transactional(rollbackFor = Exception.class)
    default boolean insertBatch(Collection<T> coll) {
        return insertBatch(coll, DEFAULT_BATCH_SIZE);
    }

    /**
     * 批量新增
     *
     * @param coll      {@link Collection<T>}
     * @param batchSize {@link int} 批次提交数量
     * @return java.lang.Integer
     * @author W.d
     * @since 2022/3/25 9:27 AM
     **/
    boolean insertBatch(Collection<T> coll, int batchSize);

    /**
     * 分页查询
     *
     * @param pageReq {@link PageReq}
     * @return PageRes
     * @author W.d
     * @since 2023/1/31 10:35
     **/
    <V extends Serializable> PageRes<T> page(PageReq<V> pageReq);

    /**
     * 新增
     *
     * @param v {@link V}
     * @author W.d
     * @since 2023/1/12 10:52
     **/
    <V extends Serializable> void add(V v);

    /**
     * 编辑
     *
     * @param v {@link V}
     * @author W.d
     * @since 2023/1/12 10:52
     **/
    <V extends Serializable> void edit(V v);

}
