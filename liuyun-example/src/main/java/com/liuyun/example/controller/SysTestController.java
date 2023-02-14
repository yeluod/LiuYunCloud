package com.liuyun.example.controller;

import com.liuyun.domain.base.page.PageReq;
import com.liuyun.domain.base.page.PageRes;
import com.liuyun.domain.example.vo.SysTestAddReqVO;
import com.liuyun.domain.example.vo.SysTestEditReqVO;
import com.liuyun.domain.example.vo.SysTestPageReqVO;
import com.liuyun.domain.example.vo.SysTestPageResVO;
import com.liuyun.example.service.SysTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 系统测试表相关接口
 *
 * @author W.d
 * @since 2023-01-31 17:17:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "系统测试表相关接口")
@RequestMapping("/sysTestEntity")
public class SysTestController {

    private final SysTestService sysTestService;

    /**
     * 分页查询
     *
     * @param pageReq 分页请求参数
     * @return PageRes<SysTestPageResVO>
     * @author W.d
     * @since 2023-01-31 17:17:39
     **/
    @PostMapping(value = "/page")
    @Operation(summary = "分页查询")
    public PageRes<SysTestPageResVO> page(@Validated @RequestBody PageReq<SysTestPageReqVO> pageReq) {
        return this.sysTestService.page(pageReq).converter(SysTestPageResVO.class);
    }

    /**
     * 新增信息
     *
     * @param vo 新增请求参数对象
     * @author W.d
     * @since 2023-01-31 17:17:39
     **/
    @PostMapping
    @Operation(summary = "新增信息")
    public void add(@Validated @RequestBody SysTestAddReqVO vo) {
        this.sysTestService.add(vo);
    }

    /**
     * 修改信息
     *
     * @param vo 修改请求参数对象
     * @author W.d
     * @since 2023-01-31 17:17:39
     **/
    @PutMapping
    @Operation(summary = "修改信息")
    public void edit(@Validated @RequestBody SysTestEditReqVO vo) {
        this.sysTestService.edit(vo);
    }

    /**
     * 删除信息
     *
     * @param id 主键ID
     * @author W.d
     * @since 2023-01-31 17:17:39
     **/
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "删除信息")
    @Parameter(name = "id", required = true, description = "主键ID")
    public void removeById(@PathVariable("id") Long id) {
        this.sysTestService.removeById(id);
    }

}
