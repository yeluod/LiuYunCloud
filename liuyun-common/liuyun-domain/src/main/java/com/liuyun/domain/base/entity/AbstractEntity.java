package com.liuyun.domain.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * SuperEntity
 *
 * @author W.d
 * @since 2022/12/7 20:20
 **/
@Data
public abstract class AbstractEntity<T> implements BeanConvert<T>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;

}
