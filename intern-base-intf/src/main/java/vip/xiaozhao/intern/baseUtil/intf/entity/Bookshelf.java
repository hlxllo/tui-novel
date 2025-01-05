package vip.xiaozhao.intern.baseUtil.intf.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Bookshelf implements Serializable {

    private int id;
    private int userId;
    private int novelId;
    private int latestChapterId;
    private String bookName;
    private String authorName;
    private String latestChapter;

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    private String coverUrl;
    private int isRead;
    private int isTop;

}
