package vip.xiaozhao.intern.baseUtil.intf.entity;


import lombok.Data;

import java.util.Date;


@Data
public class SubscribeAudit {

    private int id;
    private int userId;
    private int novelId;
    private int chapterId;
    // 用户等级
    private int level;
}
