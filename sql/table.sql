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

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumb_num   int      default 0                 not null comment '点赞数',
    favour_num  int      default 0                 not null comment '收藏数',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除',
    index idx_user_id (user_id)
    ) comment '帖子'

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_post_id (post_id),
    index idx_user_id (user_id)
    ) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    post_id     bigint                             not null comment '帖子 id',
    user_id     bigint                             not null comment '创建用户 id',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_post_id (post_id),
    index idx_user_id (user_id)
    ) comment '帖子收藏';
