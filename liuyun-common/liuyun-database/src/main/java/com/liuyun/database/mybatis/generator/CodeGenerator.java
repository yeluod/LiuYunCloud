package com.liuyun.database.mybatis.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.liuyun.database.mybatis.mapper.BaseCrudMapper;
import com.liuyun.database.mybatis.service.BaseCrudService;
import com.liuyun.database.mybatis.service.BaseCrudServiceImpl;
import com.liuyun.domain.base.entity.BaseEntity;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

/**
 * gen
 *
 * @author W.d
 * @since 2023/1/7 11:34
 **/
@Slf4j
@UtilityClass
public class CodeGenerator {

    public static void main(String[] args) {
        var schema = "liuyun-sys";
        String[] tables = {
                "sys_client",
                "sys_permission",
                "sys_role",
                "sys_role_permission",
                "sys_user",
                "sys_user_role",
        };
        var generator = new Generator(schema, tables);
        execute(generator);
    }

    private void execute(Generator generator) {
        log.info("generator -> [{}]", generator.toString());
        FastAutoGenerator
                .create(dataSourceConfig(generator))
                .globalConfig(globalConfig(generator))
                .packageConfig(packageConfig(generator))
                .strategyConfig(strategyConfig(generator))
                .templateConfig(templateConfig())
                .injectionConfig(injectionConfig(generator))
                .templateEngine(new CodeTemplateEngine())
                .execute();

    }

    private static Consumer<InjectionConfig.Builder> injectionConfig(Generator generator) {
        return builder ->
                builder.customMap(Map.of(
                                "alias", true,
                                "overrideEqualsAndHashCode", true)
                        ).customFile(Arrays.asList(
                                new CustomFile.Builder()
                                        .fileName("AddReqVO.java")
                                        .templatePath("mybatisplus/templates/vo/addReqVO.java.vm")
                                        .packageName("vo")
                                        .filePath(generator.outputDir)
                                        .build(),
                                new CustomFile.Builder()
                                        .fileName("EditReqVO.java")
                                        .templatePath("mybatisplus/templates/vo/editReqVO.java.vm")
                                        .packageName("vo")
                                        .filePath(generator.outputDir)
                                        .build(),
                                new CustomFile.Builder()
                                        .fileName("DetailResVO.java")
                                        .templatePath("mybatisplus/templates/vo/detailResVO.java.vm")
                                        .packageName("vo")
                                        .filePath(generator.outputDir)
                                        .build(),
                                new CustomFile.Builder()
                                        .fileName("PageReqVO.java")
                                        .templatePath("mybatisplus/templates/vo/pageReqVO.java.vm")
                                        .packageName("vo")
                                        .filePath(generator.outputDir)
                                        .build(),
                                new CustomFile.Builder()
                                        .fileName("PageResVO.java")
                                        .templatePath("mybatisplus/templates/vo/pageResVO.java.vm")
                                        .packageName("vo")
                                        .filePath(generator.outputDir)
                                        .build()
                        ))
                        .build();
    }

    private static Consumer<TemplateConfig.Builder> templateConfig() {
        return builder ->
                builder.entity("mybatisplus/templates/entity.java.vm")
                        .controller("mybatisplus/templates/controller.java.vm")
                        .service("mybatisplus/templates/service.java.vm")
                        .serviceImpl("mybatisplus/templates/serviceImpl.java.vm")
                        .mapper("mybatisplus/templates/mapper.java.vm")
                        .xml("mybatisplus/templates/mapper.xml.vm")
                        .build();
    }

    /**
     * 策略配置
     **/
    private static Consumer<StrategyConfig.Builder> strategyConfig(Generator generator) {
        return builder -> builder.addInclude(generator.tables)
                .entityBuilder()
                .superClass(BaseEntity.class)
                .formatFileName("%sEntity")
                .naming(NamingStrategy.underline_to_camel)
                .enableLombok()
                .enableChainModel()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                .addSuperEntityColumns(
                        "id",
                        "create_user_id",
                        "create_user_name",
                        "create_time",
                        "update_user_id",
                        "update_user_name",
                        "update_time",
                        "remark",
                        "version",
                        "deleted"
                )
                .controllerBuilder()
                //.superClass(BaseController.class)
                .formatFileName("%sController")
                .enableRestStyle()
                .serviceBuilder()
                .superServiceClass(BaseCrudService.class)
                .superServiceImplClass(BaseCrudServiceImpl.class)
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl")
                .mapperBuilder()
                .superClass(BaseCrudMapper.class)
                .mapperAnnotation(Mapper.class)
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .build();
    }

    /**
     * 数据库配置
     **/
    private DataSourceConfig.Builder dataSourceConfig(Generator generator) {

        return new DataSourceConfig.Builder(generator.dbUrl, generator.dbUsername, generator.dbPassword)
                .schema(generator.schema)
                .dbQuery(new MySqlQuery())
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler());
    }

    /**
     * 全局配置
     **/
    private Consumer<GlobalConfig.Builder> globalConfig(Generator generator) {
        return builder -> builder.outputDir(generator.outputDir)
                .author(generator.author)
                .disableOpenDir()
                .enableSpringdoc()
                .dateType(DateType.TIME_PACK)
                .commentDate(() -> LocalDateTime.now().format(DateTimeFormatter.ofPattern(generator.commentDatePattern)))
                .build();
    }

    private Consumer<PackageConfig.Builder> packageConfig(Generator generator) {
        return builder -> builder.parent(generator.parent)
                //.moduleName("modules")
                .entity("entity")
                .controller("controller")
                .service("service")
                .serviceImpl("service.impl")
                .mapper("mapper")
                .pathInfo(Collections.singletonMap(OutputFile.xml, (generator.outputDir + "/resources/mapper/")))
                .build();
    }

    @ToString
    @Accessors(chain = true)
    public static class Generator {

        public Generator(String schema, String... tables) {
            var rb = ResourceBundle.getBundle("mybatisplus/mybatis-plus");
            this.dbUrl = rb.getString("datasource.url") + schema;
            this.dbUsername = rb.getString("datasource.username");
            this.dbPassword = rb.getString("datasource.password");
            this.schema = schema;
            this.author = rb.getString("author");
            this.commentDatePattern = rb.getString("comment-date-pattern");
            this.parent = rb.getString("parent");
            this.outputDir = rb.getString("output-dir");
            this.tables = Arrays.stream(tables).toList();
        }

        private final String dbUrl;
        private final String dbUsername;
        private final String dbPassword;
        private final String schema;
        private final String author;
        private final String commentDatePattern;
        private final String parent;
        private final String outputDir;
        private final List<String> tables;
    }
}
