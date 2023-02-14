package com.liuyun.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.liuyun.domain.*;

import org.springframework.web.bind.annotation.RestController;
import com.liuyun.service.SysUserRoleService;

/**
 * 系统用户角色关联表相关接口
 *
 * @author W.d
 * @since 2023-02-01 09:40:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "系统用户角色关联表相关接口")
@RequestMapping("/sysUserRoleEntity")
public class SysUserRoleController {

    private final SysUserRoleService sysUserRoleService;

    /**
     * 分页查询
     *
     * @param pageReq 分页请求参数
     * @return PageRes<SysUserRolePageResVO>
     * @author W.d
     * @since 2023-02-01 09:40:12
    **/
    @PostMapping(value = "/page")
    @Operation(summary = "分页查询")
    public PageRes<SysUserRolePageResVO> page(@Validated @RequestBody PageReq<SysUserRolePageReqVO> pageReq){
        return this.sysUserRoleService.page(pageReq).converter(SysUserRolePageResVO.class);
    }

    /**
     * 新增信息
     *
     * @param vo 新增请求参数对象
     * @author W.d
     * @since 2023-02-01 09:40:12
    **/
    @PostMapping
    @Operation(summary = "新增信息")
    public void add(@Validated @RequestBody SysUserRoleAddReqVO vo){
        this.sysUserRoleService.add(vo);
    }

    /**
     * 修改信息
     *
     * @param vo 修改请求参数对象
     * @author W.d
     * @since 2023-02-01 09:40:12
    **/
    @PutMapping
    @Operation(summary = "修改信息")
    public void edit(@Validated @RequestBody SysUserRoleEditReqVO vo){
        this.sysUserRoleService.edit(vo);
    }

    /**
     * 删除信息
     *
     * @param id 主键ID
     * @author W.d
     * @since 2023-02-01 09:40:12
    **/
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "删除信息")
    @Parameter(name = "id", required = true, description = "主键ID")
    public void removeById(@PathVariable("id") Long id){
        this.sysUserRoleService.removeById(id);
    }

}
