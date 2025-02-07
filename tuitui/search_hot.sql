create table search_hot
(
    id         int auto_increment comment '主键'
        primary key,
    bookName   varchar(255)                       not null comment '书名',
    bookId     int                                not null comment '书籍ID',
    times      int                                not null comment '搜索次数',
    status     tinyint  default 1                 null comment '状态: 1-有效, 0-无效',
    addTime    datetime default CURRENT_TIMESTAMP null comment '添加时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '搜索热度';

