package vip.xiaozhao.intern.baseUtil.intf.DO;

import lombok.Data;

@Data
public class NovelDO {

    private int id;
    private int authorId;
    private String bookName;
    private String authorName;
    private String description;
    private String bookUrl;
    private String cover;

}
