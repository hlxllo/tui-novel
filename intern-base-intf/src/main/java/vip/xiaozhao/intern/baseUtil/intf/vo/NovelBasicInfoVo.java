package vip.xiaozhao.intern.baseUtil.intf.vo;

import lombok.Data;

@Data
public class NovelBasicInfoVo {
    private int id;
    private String bookName;
    private String authorName;
    private int subscribeNum;
    private String cover;
    private int latestChapterId;
    private String latestChapter;
}
