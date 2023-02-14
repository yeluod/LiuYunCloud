package com.liuyun.core;

import com.liuyun.core.advice.ApiResponseBodyAdvice;
import com.liuyun.core.exception.ServiceExceptionHandler;
import com.liuyun.core.web.JsonConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;

/**
 * CoreAutoConfig
 *
 * @author W.d
 * @since 2022/12/17 18:02
 **/
@Lazy
@Import(value = {
        JsonConfiguration.class
})
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceExceptionHandler serviceExceptionHandler() {
        return new ServiceExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiResponseBodyAdvice apiResponseBodyAdvice() {
        return new ApiResponseBodyAdvice();
    }

}
