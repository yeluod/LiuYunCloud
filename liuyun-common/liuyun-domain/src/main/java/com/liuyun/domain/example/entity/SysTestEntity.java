package com.liuyun.domain.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.liuyun.domain.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 系统测试表
 *
 * @author W.d
 * @since 2023-01-09 20:39:03
 */
@Getter
@Setter
@Accessors(chain = true)
@Alias(value = "SysTestEntity")
@TableName("sys_test")
@EqualsAndHashCode(callSuper = false)
public class SysTestEntity extends BaseEntity<SysTestEntity> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 手机号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 生日
     */
    @TableField("birthday")
    private LocalDate birthday;
}
