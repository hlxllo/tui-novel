create table novel_info
(
    id                  int auto_increment
        primary key,
    AuthorId            int                                null,
    BookName            varchar(255)                       not null,
    AuthorName          varchar(255)                       null,
    `Desc`              text                               null,
    SubscribeNum        int      default 0                 null,
    WeekSubNum          int      default 0                 null,
    MonthSubNum         int      default 0                 null,
    SearchNum           int      default 0                 null,
    BookUrl             varchar(255)                       null,
    Cover               varchar(255)                       null,
    LastUpdateTime      datetime                           null,
    SearchUpdateTime    datetime                           null,
    Regex               varchar(255)                       null,
    SearchKey           varchar(255)                       null,
    LatestChapterId     int      default 0                 null,
    LatestChapter       varchar(550)                       null,
    ChapterUrl          varchar(255)                       null,
    SearchLatestChapter varchar(255)                       null,
    Status              tinyint                            null,
    AddTime             datetime default CURRENT_TIMESTAMP null,
    UpdateTime          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
)
    comment '小说信息';

create fulltext index BookName
    on novel_info (BookName, AuthorName);

