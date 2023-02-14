package com.liuyun.database.mybatis.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * CodeTemplateEngine
 *
 * @author W.d
 * @since 2023/1/9 16:24
 **/
public class CodeTemplateEngine extends VelocityTemplateEngine {

    private static final String DOMAIN_SUFFIX = "Entity";

    @Override
    @SuppressWarnings("unchecked")
    protected void outputCustomFile(List<CustomFile> customFiles, TableInfo tableInfo, Map<String, Object> objectMap) {
        String entityName = tableInfo.getEntityName();

        objectMap.put("voPackage", customFiles.stream()
                                           .findFirst()
                                           .map(item -> ((Map<String, String>) objectMap.get("package")).get("Parent") + StrUtil.C_DOT + item.getPackageName())
                                           .orElse(""));
        objectMap.put("detailResVO", StrUtil.replace(entityName, DOMAIN_SUFFIX, "DetailResVO"));
        objectMap.put("pageResVO", StrUtil.replace(entityName, DOMAIN_SUFFIX, "PageResVO"));
        objectMap.put("pageReqVO", StrUtil.replace(entityName, DOMAIN_SUFFIX, "PageReqVO"));
        objectMap.put("addReqVO", StrUtil.replace(entityName, DOMAIN_SUFFIX, "AddReqVO"));
        objectMap.put("editReqVO", StrUtil.replace(entityName, DOMAIN_SUFFIX, "EditReqVO"));
        objectMap.put("lowerCaseServiceName", StrUtil.lowerFirst(tableInfo.getServiceName()));
        String parentPath = getPathInfo(OutputFile.parent);
        customFiles.forEach(file -> {
            String fileName = parentPath + File.separator + file.getPackageName() + File.separator + StrUtil.replace(entityName, DOMAIN_SUFFIX, "") + file.getFileName();
            outputFile(new File(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
        });
    }
}
