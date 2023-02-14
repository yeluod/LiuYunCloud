package com.liuyun.example.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.liuyun.domain.example.vo.TestResVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * TestController
 *
 * @author W.d
 * @since 2022/11/23 17:19
 **/
@Slf4j
@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "链路ID测试相关接口")
public class TraceController {

    @GetMapping(value = "/echo/{id}")
    @Operation(summary = "打印链路ID")
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Parameter(name = "id", required = true, description = "业务ID")
    public TestResVO echo(@PathVariable("id") Long id) {
        log.info("id -> [{}]", id);
        CompletableFuture.runAsync(() -> {
            log.info("CompletableFuture id -> [{}]", id);
        });
        ThreadUtil.sleep(2000L);
        return new TestResVO();
    }
}
