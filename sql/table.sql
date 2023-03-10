-- 用户表
CREATE TABLE user
(
    id             bigint auto_increment comment '主键ID' primary key,
    user_account   varchar(256)                           not null comment '账号',
    user_password  varchar(512)                           not null comment '密码',
    user_name      varchar(256)                           null comment '用户明',
    user_avatar    varchar(1024)                          null comment '用户头像',
    user_profile   varchar(512)                           null comment '用户简介',
    phone          varchar(20)                            null comment '用户手机号',
    email          varchar(255)                           null comment '邮箱',
    user_role      varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint      default 0                 not null comment '是否删除'
) comment '用户表'
