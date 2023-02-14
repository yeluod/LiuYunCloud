package com.liuyun.example;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liuyun.base.exception.VerifyException;
import com.liuyun.domain.example.entity.SysTestEntity;
import com.liuyun.example.service.SysTestService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SqlRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MybatisPlusTests
 *
 * @author W.d
 * @since 2022/9/26 13:56
 **/
@Slf4j
@SpringBootTest
class MybatisPlusTests {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SysTestService sysTestService;

    private List<SysTestEntity> getTestData() {
        var list = new ArrayList<SysTestEntity>(100);
        for (int i = 0; i < 50; i++) {
            var sysTest = new SysTestEntity().setName("name" + i).setMobile(i + "").setAge(i).setBirthday(LocalDate.now());
            list.add(sysTest);
        }
        return list;
    }

    private String createSql() {
        return """
                DROP TABLE IF EXISTS sys_test;
                CREATE TABLE IF NOT EXISTS sys_test
                (
                    id               BIGINT AUTO_INCREMENT COMMENT '主键'
                        PRIMARY KEY,
                    `name`             VARCHAR(64)    NULL COMMENT '名称',
                    mobile             VARCHAR(255)   NULL COMMENT '手机号',
                    age              INT              NULL COMMENT '年龄',
                    birthday         DATE             NULL COMMENT '生日',
                    create_user_id   BIGINT           NULL COMMENT '创建人ID',
                    create_user_name VARCHAR(64)      NULL COMMENT '创建人名称',
                    create_time      DATETIME         NULL COMMENT '创建时间',
                    update_user_id   BIGINT           NULL COMMENT '修改人ID',
                    update_user_name VARCHAR(64)      NULL COMMENT '修改人名称',
                    update_time      DATETIME         NULL COMMENT '修改时间',
                    version          BIGINT DEFAULT 0 NULL COMMENT '乐观锁字段',
                    remark           VARCHAR(255)     NULL COMMENT '备注',
                    deleted          INT    DEFAULT 0 NULL COMMENT '逻辑删除 0:否 1:是'
                )
                    COMMENT '系统测试表';
                """;
    }

    @BeforeEach
    @SneakyThrows
    void before() {
        var connection = dataSource.getConnection();
        var sqlRunner = new SqlRunner(connection);
        sqlRunner.run(this.createSql());
        connection.close();
    }

    @AfterEach
    @SneakyThrows
    void after() {
        var connection = dataSource.getConnection();
        var sqlRunner = new SqlRunner(connection);
        sqlRunner.run("TRUNCATE sys_test;");
        connection.close();
    }

    @Test
    void checkByIdTest() {
        Assertions.assertThrows(VerifyException.class, () -> this.sysTestService.checkById(1L));
    }

    @Test
    void insertBatchTest() {
        Assertions.assertAll(() -> Assertions.assertThrows(VerifyException.class,
                        () -> this.sysTestService.insertBatch(null)),
                () -> Assertions.assertTrue(this.sysTestService.insertBatch(this.getTestData())),
                () -> Assertions.assertTrue(this.sysTestService.insertBatch(this.getTestData(), 10)));
    }

    @Test
    void optimisticLockerTest() {
        Assertions.assertTrue(() -> {
            Long version;
            var sysTest = new SysTestEntity().setName("name").setAge(18).setBirthday(LocalDate.now());
            this.sysTestService.save(sysTest);
            version = sysTest.getVersion();
            this.sysTestService.updateById(sysTest);
            return ObjectUtil.equals(version + 1, sysTest.getVersion());
        });
    }

    @Test
    void blockAttackInnerTest() {
        Assertions.assertAll(() -> Assertions.assertTrue(this.sysTestService.insertBatch(this.getTestData())),
                () -> Assertions.assertThrows(MyBatisSystemException.class,
                        () -> this.sysTestService.update(Wrappers.lambdaUpdate(SysTestEntity.class).set(SysTestEntity::getAge, 18))));
    }


}
