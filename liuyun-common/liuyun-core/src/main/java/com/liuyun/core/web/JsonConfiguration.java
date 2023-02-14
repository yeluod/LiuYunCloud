package com.liuyun.core.web;

import cn.hutool.core.lang.Opt;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.liuyun.core.constants.TimeFormatConstant;
import com.liuyun.core.web.editors.*;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * JsonConfiguration
 *
 * @author W.d
 * @since 2022/7/18 14:42
 **/
@Lazy
@RestControllerAdvice
@Configuration(proxyBeanMethods = false)
public class JsonConfiguration {

    /**
     * 表单参数处理
     *
     * @author W.d
     * @since 2022/5/11 18:09
     **/
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 日期转换
        binder.registerCustomEditor(Date.class, new DateEditor());
        binder.registerCustomEditor(LocalTime.class, new LocalTimeEditor());
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
        binder.registerCustomEditor(LocalDateTime.class, new LocalDateTimeEditor());
        // 其他类型转换
        binder.registerCustomEditor(Long.class, new LongEditor());
        binder.registerCustomEditor(Float.class, new FloatEditor());
        binder.registerCustomEditor(Double.class, new DoubleEditor());
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
    }

    /**
     * Json 转换
     *
     * <p><a href="https://github.com/FasterXML/jackson-modules-java8"/>https://github.com/FasterXML/jackson-modules-java8</a><p/>
     *
     * @return HttpMessageConverters
     * @author W.d
     * @since 2022/5/11 18:09
     **/
    @Bean
    public HttpMessageConverters jacksonHttpMessageConverters() {
        return Opt.ofNullable(new JavaTimeModule())
                .peeks(item -> item.addSerializer(Long.class, ToStringSerializer.instance),
                        item -> item.addSerializer(LocalTime.class, new LocalTimeSerializer(TimeFormatConstant.DEFAULT_HMS)),
                        item -> item.addSerializer(LocalDate.class, new LocalDateSerializer(TimeFormatConstant.DEFAULT_YMD)),
                        item -> item.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(TimeFormatConstant.DEFAULT_YMDHMS)),
                        item -> item.addDeserializer(LocalTime.class, new LocalTimeDeserializer(TimeFormatConstant.DEFAULT_HMS)),
                        item -> item.addDeserializer(LocalDate.class, new LocalDateDeserializer(TimeFormatConstant.DEFAULT_YMD)),
                        item -> item.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(TimeFormatConstant.DEFAULT_YMDHMS))

                )
                .map(item -> JsonMapper.builder()
                        .serializationInclusion(JsonInclude.Include.ALWAYS)
                        .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
                        .build()
                        .registerModule(item))
                .map(item -> {
                    var jacksonConverter = new MappingJackson2HttpMessageConverter(item);
                    jacksonConverter.setDefaultCharset(StandardCharsets.UTF_8);
                    var stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
                    stringConverter.setDefaultCharset(StandardCharsets.UTF_8);
                    return new HttpMessageConverters(jacksonConverter, stringConverter);
                })
                .get();
    }
}
