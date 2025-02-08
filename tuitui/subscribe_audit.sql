create table subscribe_audit
(
    id         int auto_increment comment '主键'
        primary key,
    userId     int                                null comment '用户ID',
    novelId    int                                null comment '小说ID',
    ChapterId  int                                null comment '章节ID',
    addTime    datetime default CURRENT_TIMESTAMP null comment '添加时间',
    level      tinyint                            not null comment '消息等级',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '阅读信息';

