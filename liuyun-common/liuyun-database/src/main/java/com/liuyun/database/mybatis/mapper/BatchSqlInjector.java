package com.liuyun.database.mybatis.mapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * 批量插入注入器
 *
 * @author W.d
 * @since 2022/7/13 18:28
 **/
public class BatchSqlInjector extends DefaultSqlInjector {

    /**
     * 追加批量插入
     *
     * @param mapperClass {@link Class} mapper 接口
     * @param tableInfo   {@link TableInfo} 数据库表反射信息
     * @return java.util.List<com.baomidou.mybatisplus.core.injector.AbstractMethod>
     * @author W.d
     * @since 2022/7/13 18:29
     **/
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        methodList.add(new InsertBatch("insertBatch"));
        return methodList;
    }

}
