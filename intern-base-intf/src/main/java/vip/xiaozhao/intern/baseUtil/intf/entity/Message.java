package vip.xiaozhao.intern.baseUtil.intf.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author hlxllo
 * @description 通知类
 * @date 2025/2/7
 */
@Data
public class Message {
    private int id;
    private int userId;
    private int novelId;
    private int chapterId;
    private Date sendTime;
}
