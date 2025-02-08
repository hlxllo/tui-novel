package vip.xiaozhao.intern.baseUtil.intf.mapper;

import vip.xiaozhao.intern.baseUtil.intf.entity.Message;

/**
 * @author hlxllo
 * @description 通知持久层
 * @date 2025/2/7
 */
public interface MessageMapper {

    Message getLastMessage(int userId, int novelId);

    Integer getMessageLevel(int userId, int novelId);

    void upgradeLevel(int userId, int novelId, int level);

    void insertMessageLevel(int userId, int novelId);
}
