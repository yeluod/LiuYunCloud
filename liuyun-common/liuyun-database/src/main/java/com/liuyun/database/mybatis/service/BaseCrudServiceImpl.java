package com.liuyun.database.mybatis.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuyun.base.exception.VerifyException;
import com.liuyun.database.mybatis.mapper.BaseCrudMapper;
import com.liuyun.domain.base.page.PageReq;
import com.liuyun.domain.base.page.PageRes;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 自定义 Service
 *
 * @author W.d
 * @since 2022/7/13 13:27
 **/
public abstract class BaseCrudServiceImpl<M extends BaseCrudMapper<T>, T> extends ServiceImpl<M, T> implements BaseCrudService<T> {

    /**
     * 批量新增
     *
     * @param coll      {@link Collection<T>}
     * @param batchSize {@link Integer} 批次提交数量
     * @return java.lang.Integer
     * @author W.d
     * @since 2022/3/25 9:27 AM
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertBatch(Collection<T> coll, int batchSize) {
        Assert.notEmpty(coll, () -> new VerifyException("批量新增数据不能为空"));
        Integer sum = CollUtil.split(coll, batchSize)
                .stream()
                .map(item -> super.baseMapper.insertBatch(item))
                .reduce(Integer::sum)
                .orElse(0);
        return ObjectUtil.equal(sum, coll.size());
    }

    /**
     * 分页查询
     *
     * @param pageReq {@link PageReq}
     * @return PageRes
     * @author W.d
     * @since 2023/1/31 10:35
     **/
    @Override
    public <V extends Serializable> PageRes<T> page(PageReq<V> pageReq) {
        return new LambdaQueryChainWrapper<T>(super.baseMapper)
                .page(new PageRes<>(pageReq));
    }

    /**
     * 新增
     *
     * @param v {@link V}
     * @author W.d
     * @since 2023/1/12 10:52
     **/
    @Override
    public <V extends Serializable> void add(V v) {
        Assert.isTrue(super.save(BeanUtil.copyProperties(v, super.entityClass)),
                () -> new VerifyException("新增信息失败"));
    }

    /**
     * 编辑
     *
     * @param v {@link V}
     * @author W.d
     * @since 2023/1/12 10:52
     **/
    @Override
    public <V extends Serializable> void edit(V v) {
        Assert.isTrue(super.updateById(BeanUtil.copyProperties(v, super.entityClass)),
                () -> new VerifyException("修改信息失败"));
    }

}
