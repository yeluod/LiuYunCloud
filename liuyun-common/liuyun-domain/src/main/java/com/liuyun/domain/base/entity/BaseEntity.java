package com.liuyun.domain.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * BaseEntity
 *
 * @author W.d
 * @since 2022/12/7 20:21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseEntity<T> extends AbstractEntity<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建人ID
     */
    @TableField(value = "create_user_id", fill = FieldFill.INSERT)
    protected Long createUserId;

    /**
     * 创建人名称
     */
    @TableField(value = "create_user_name", fill = FieldFill.INSERT)
    protected String createUserName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /**
     * 修改人ID
     */
    @TableField(value = "update_user_id", fill = FieldFill.UPDATE)
    protected Long updateUserId;

    /**
     * 修改人名称
     */
    @TableField(value = "update_user_name", fill = FieldFill.UPDATE)
    protected String updateUserName;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    protected LocalDateTime updateTime;

    /**
     * 乐观锁字段
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 乐观锁字段
     */
    @Version
    @TableField(value = "version", fill = FieldFill.INSERT)
    private Long version;

    /**
     * 逻辑删除 0:否 1:是
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    private Integer deleted;

}
