DROP TABLE IF EXISTS sys_test;
CREATE TABLE IF NOT EXISTS sys_test
(
    id               BIGINT AUTO_INCREMENT COMMENT '主键'
        PRIMARY KEY,
    `name`           VARCHAR(64)      NULL COMMENT '名称',
    mobile           VARCHAR(255)     NULL COMMENT '手机号',
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

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE IF NOT EXISTS `sys_log`
(
    id               BIGINT COMMENT '主键'
        PRIMARY KEY,
    title            VARCHAR(255)     NULL COMMENT '日志标题',
    user_agent       VARCHAR(255)     NULL COMMENT '用户代理',
    remote_ip        VARCHAR(255)     NULL COMMENT '操作IP地址',
    request_uri      VARCHAR(255)     NULL COMMENT '请求URI',
    server_name      VARCHAR(255)     NULL COMMENT '服务名称',
    class_name       VARCHAR(255)     NULL COMMENT '类名称',
    method_name      VARCHAR(255)     NULL COMMENT '方法名称',
    method           VARCHAR(10)      NULL COMMENT '操作方式',
    params           TEXT             NULL COMMENT '操作提交的数据',
    `exception`      TEXT             NULL COMMENT '异常信息',
    `time`           BIGINT           NULL COMMENT '执行时间',
    type             VARCHAR(11)      NULL COMMENT '日志类型(0:正常; 1:错误)',
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
    COMMENT '系统日志表';

DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE IF NOT EXISTS `sys_client`
(
    id                     BIGINT COMMENT '主键'
        PRIMARY KEY,
    client_id              VARCHAR(64)           NOT NULL COMMENT '客户端id',
    client_name            VARCHAR(255)          NOT NULL COMMENT '客户端名称',
    client_secret          VARCHAR(255)          NOT NULL COMMENT '客户端秘钥',
    scopes                 JSON                  NULL COMMENT '作用域',
    redirect_uris          JSON                  NULL COMMENT '重定向URI',
    grant_types            JSON                  NULL COMMENT '授权方式',
    additional_information JSON                  NULL COMMENT '扩展信息 example: {"key":"value"}',
    access_token_timeout   BIGINT      DEFAULT 300 COMMENT 'access_token 过期时间(单位:秒 默认300)',
    refresh_token_timeout  BIGINT      DEFAULT 7200 COMMENT 'refresh_token 过期时间(单位:秒 默认7200)',
    code_timeout           BIGINT      DEFAULT 300 COMMENT '授权码过期时间(单位:秒 默认300)',
    auto_approve           VARCHAR(11) DEFAULT 'N' COMMENT '自动授权,授权码模式设置true跳过用户确认授权操作页面,直接跳到redirect_ur(Y: 是; N: 否)',
    `status`               VARCHAR(11)           NOT NULL COMMENT '状态(ENABLE:启用; DISABLE:禁用)',
    create_user_id         BIGINT                NULL COMMENT '创建人ID',
    create_user_name       VARCHAR(64)           NULL COMMENT '创建人名称',
    create_time            DATETIME              NULL COMMENT '创建时间',
    update_user_id         BIGINT                NULL COMMENT '修改人ID',
    update_user_name       VARCHAR(64)           NULL COMMENT '修改人名称',
    update_time            DATETIME              NULL COMMENT '修改时间',
    version                BIGINT      DEFAULT 0 NULL COMMENT '乐观锁字段',
    remark                 VARCHAR(255)          NULL COMMENT '备注',
    deleted                INT         DEFAULT 0 NULL COMMENT '逻辑删除 0:否 1:是'
) COMMENT '系统客户端表';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE IF NOT EXISTS `sys_user`
(
    id               BIGINT COMMENT '主键'
        PRIMARY KEY,
    `name`             VARCHAR(64)      NOT NULL COMMENT '账号',
    username         VARCHAR(64)      NULL COMMENT '账号',
    `password`       VARCHAR(255)     NULL COMMENT '密码',
    phone            VARCHAR(11)      NULL COMMENT '手机号',
    email            VARCHAR(64)      NULL COMMENT '邮箱',
    avatar           VARCHAR(255)     NULL COMMENT '头像',
    gender           VARCHAR(11)      NULL COMMENT '性别',
    `status`         VARCHAR(11)      NOT NULL COMMENT '状态(ENABLE:启用; DISABLE:禁用)',
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
    COMMENT '系统用户表';

DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE IF NOT EXISTS `sys_dept`
(
    id               BIGINT COMMENT '主键'
        PRIMARY KEY,
    pid              BIGINT           NOT NULL COMMENT '父级ID',
    `name`           VARCHAR(64)      NOT NULL COMMENT '名称',
    `code`           VARCHAR(64)      NOT NULL COMMENT '编码',
    `status`         VARCHAR(11)      NOT NULL COMMENT '状态(ENABLE:启用; DISABLE:禁用)',
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
    COMMENT '系统部门表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE IF NOT EXISTS `sys_role`
(
    id               BIGINT COMMENT '主键'
        PRIMARY KEY,
    `name`           VARCHAR(64)      NOT NULL COMMENT '名称',
    `code`           VARCHAR(64)      NOT NULL COMMENT '编码',
    `status`         VARCHAR(11)      NOT NULL COMMENT '状态(ENABLE:启用; DISABLE:禁用)',
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
    COMMENT '系统角色表';

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE IF NOT EXISTS `sys_permission`
(
    id               BIGINT COMMENT '主键'
        PRIMARY KEY,
    pid              BIGINT           NULL COMMENT '父级ID',
    `name`           VARCHAR(64)      NOT NULL COMMENT '名称',
    `code`           VARCHAR(64)      NULL COMMENT '编码',
    icon             VARCHAR(64)      NULL COMMENT '图标',
    router           VARCHAR(255)     NULL COMMENT '路由',
    `path`           VARCHAR(255)     NULL COMMENT '路径',
    `sort`           INT              NULL COMMENT '排序',
    type             VARCHAR(11)      NOT NULL COMMENT '类型(MENU:菜单; BUTTON:按钮; PATH:路径)',
    `status`         VARCHAR(11)      NOT NULL COMMENT '状态(ENABLE:启用; DISABLE:禁用)',
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
    COMMENT '系统权限表';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE IF NOT EXISTS `sys_user_role`
(
    id      BIGINT COMMENT '主键'
        PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID'
)
    COMMENT '系统用户角色关联表';

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE IF NOT EXISTS `sys_role_permission`
(
    id            BIGINT COMMENT '主键'
        PRIMARY KEY,
    role_id       BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '用户ID'
)
    COMMENT '系统角色权限关联表';