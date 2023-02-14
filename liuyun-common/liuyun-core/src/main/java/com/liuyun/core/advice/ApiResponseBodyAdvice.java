package com.liuyun.core.advice;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.liuyun.base.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * ApiResponseBodyAdvice
 *
 * @author W.d
 * @since 2022/7/17 17:18
 **/
@RestControllerAdvice
@SuppressWarnings("NullableProblems")
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    /**
     * 需要忽略的地址
     */
    private static final String[] IGNORES = new String[]{
            "/actuator/**"
    };

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        for (String ignore : IGNORES) {
            String path = URLUtil.getPath(request.getURI().getPath());
            if (StrUtil.isNotBlank(path) && ANT_PATH_MATCHER.match(ignore, path)) {
                return body;
            }
        }
        if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
