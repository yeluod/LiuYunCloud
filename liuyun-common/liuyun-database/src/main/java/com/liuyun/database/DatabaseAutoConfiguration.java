package com.liuyun.database;

import com.liuyun.database.mybatis.MybatisPlusConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * DatabaseAutoConfiguration
 *
 * @author W.d
 * @since 2022/12/31 12:53
 **/
@Lazy
@Configuration(proxyBeanMethods = false)
@Import(value = {
        MybatisPlusConfiguration.class
})
public class DatabaseAutoConfiguration {


}
