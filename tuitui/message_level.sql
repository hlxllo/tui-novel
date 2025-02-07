create table message_level
(
    id         int auto_increment comment '主键'
        primary key,
    userId     int                                not null comment '用户 id',
    novelId    int                                not null comment '小说 id',
    level      int      default 1                 not null comment '等级1-5，数字越小越及时',
    status     tinyint  default 1                 null comment '状态: 1-有效, 0-无效',
    addTime    datetime default CURRENT_TIMESTAMP null comment '添加时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '消息等级表';

