package vip.xiaozhao.intern.baseUtil.intf.entity;


import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NovelInfo {

    private int id;
    private int authorId;
    private String bookName;
    private String authorName;
    private String description;
    private int subscribeNum;
    private int weekSubNum;
    private int monthSubNum;
    private int searchNum;
    private String bookUrl;
    private String cover;
    private Date lastUpdateTime;
    private Date searchUpdateTime;
    private String regex;
    private String searchKey;
    private int latestChapterId;
    private String latestChapter;
    private String chapterUrl;
    private String searchLatestChapter;
}
