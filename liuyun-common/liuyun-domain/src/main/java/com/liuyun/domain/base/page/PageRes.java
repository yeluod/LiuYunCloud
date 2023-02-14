package com.liuyun.domain.base.page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 分页对象
 *
 * @author W.d
 * @since 2022/7/15 17:02
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class PageRes<T> extends PageDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final long DEFAULT_CURRENT = 1L;
    private static final long DEFAULT_SIZE = 10L;

    public PageRes() {
    }

    public PageRes(PageReq<?> pageReq) {
        super(Optional.ofNullable(pageReq.getCurrent()).orElse(DEFAULT_CURRENT),
                Optional.ofNullable(pageReq.getSize()).orElse(DEFAULT_SIZE));
    }

    /**
     * 转换 响应参数
     *
     * @param cla {@link Class}
     * @return com.sogal.common.mybatis.vo.PageDTO<M>
     * @author W.d
     * @since 2022/1/8 12:59 PM
     **/
    public <R> PageRes<R> converter(Class<R> cla) {
        PageRes<R> dto = new PageRes<>();
        dto.setTotal(getTotal());
        dto.setPages(getPages());
        dto.setSize(getSize());
        dto.setCurrent(getCurrent());
        final List<T> records = getRecords();
        dto.setRecords(CollUtil.isEmpty(records) ? Collections.emptyList()
                : BeanUtil.copyToList(records, cla));
        return dto;
    }
}
