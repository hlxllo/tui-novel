create table bookshelf
(
    id              int auto_increment
        primary key,
    userId          int                                  null comment '用户id',
    novelId         int                                  null comment '小说id',
    LatestchapterId int                                  null comment '最新章节id',
    BookName        varchar(255)                         null comment '书名',
    AuthorName      varchar(255)                         null comment '作者名',
    Latestchapter   varchar(255)                         null comment '小说最新章节',
    LastUpdateTime  datetime                             null comment '小说最新更新时间',
    coverUrl        varchar(255)                         null comment '小说封面图片url',
    status          tinyint    default 1                 null comment '状态: 0-异常, 1-未完成, 2-已完成',
    isRead          tinyint(1) default 0                 null comment '是否已读: 0-未读, 1-已读',
    isTop           tinyint(1) default 0                 null comment '是否置顶: 0-否, 1-是',
    addTime         datetime   default CURRENT_TIMESTAMP null comment '添加时间',
    updateTime      datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '我的书架';

