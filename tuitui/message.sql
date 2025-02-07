create table message
(
    id         int auto_increment comment '主键'
        primary key,
    userId     int                                not null comment '用户 id',
    novelId    int                                not null comment '小说 id',
    chapterId  int                                not null comment '章节 id',
    sendTime   datetime default CURRENT_TIMESTAMP null comment '发送时间',
    status     tinyint  default 1                 null comment '状态: 1-有效, 0-无效',
    addTime    datetime default CURRENT_TIMESTAMP null comment '添加时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '消息发送表';

