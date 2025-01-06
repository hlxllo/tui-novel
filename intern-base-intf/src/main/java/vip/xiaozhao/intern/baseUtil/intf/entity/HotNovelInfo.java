package vip.xiaozhao.intern.baseUtil.intf.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class HotNovelInfo implements Serializable {

    private int id;
    private int bookId;
    private String bookName;
    private int times;

}
