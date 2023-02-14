package com.liuyun.database.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.liuyun.database.mybatis.handler.FillMetaObjectHandler;
import com.liuyun.database.mybatis.mapper.BatchSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlusConfiguration
 *
 * @author W.d
 * @since 2022/7/13 09:36
 **/
@Lazy
@MapperScan(value = {"com.liuyun.*.mapper"})
@EnableTransactionManagement
public class MybatisPlusConfiguration {

    /**
     * mybatis-plus 插件
     *
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     * @author W.d
     * @since 2022/3/25 11:16 AM
     **/
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        innerInterceptor.setOverflow(false);
        innerInterceptor.setMaxLimit(500L);
        innerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

    /**
     * 批量插入注入器
     *
     * @return com.ly.database.mybatisplus.interceptor.BatchSqlInjector
     * @author W.d
     * @since 2022/3/25 9:49 AM
     **/
    @Bean
    @ConditionalOnMissingBean
    public BatchSqlInjector batchSqlInjector() {
        return new BatchSqlInjector();
    }

    /**
     * 自动填充处理器
     *
     * @return com.ly.database.mybatisplus.handler.FillMetaObjectHandler
     * @author W.d
     * @since 2022/3/25 11:12 AM
     **/
    @Bean
    @ConditionalOnMissingBean
    public FillMetaObjectHandler metaObjectHandler() {
        return new FillMetaObjectHandler();
    }
}
