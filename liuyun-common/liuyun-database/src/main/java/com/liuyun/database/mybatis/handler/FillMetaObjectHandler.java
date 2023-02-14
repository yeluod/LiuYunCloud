package com.liuyun.database.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 自动填充处理器
 *
 * @author W.d
 * @since 2022/7/14 09:32
 **/
public class FillMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject {@link MetaObject} 元对象
     * @author W.d
     * @since 2022/3/25 11:09 AM
     **/
    @Override
    public void insertFill(MetaObject metaObject) {
        final LocalDateTime now = LocalDateTime.now();
        this.fillHasGetter(metaObject, "createUserId", 0L);
        this.fillHasGetter(metaObject, "createUserName", "admin");
        this.fillHasGetter(metaObject, "createTime", now);
        this.fillHasGetter(metaObject, "version", 0L);
        this.fillHasGetter(metaObject, "deleted", 0);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject {@link MetaObject} 元对象
     * @author W.d
     * @since 2022/3/25 11:09 AM
     **/
    @Override
    public void updateFill(MetaObject metaObject) {
        this.fillHasGetter(metaObject, "updateUserId", 0L);
        this.fillHasGetter(metaObject, "updateUserName", "admin");
        this.fillHasGetter(metaObject, "updateTime", LocalDateTime.now());
    }

    /**
     * 字段存在时 填充
     *
     * @param metaObject {@link MetaObject} 元对象
     * @param fieldName  {@link String} 字段名称
     * @param fieldVal   {@link Object} 填充数据
     * @author W.d
     * @since 2022/3/25 11:10 AM
     **/
    protected void fillHasGetter(MetaObject metaObject, String fieldName, Object fieldVal) {
        if (metaObject.hasGetter(fieldName)) {
            this.fillStrategy(metaObject, fieldName, fieldVal);
        }
    }
}
