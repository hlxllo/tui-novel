package vip.xiaozhao.intern.baseUtil.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vip.xiaozhao.intern.baseUtil.intf.entity.Bookshelf;
import vip.xiaozhao.intern.baseUtil.intf.entity.Message;
import vip.xiaozhao.intern.baseUtil.intf.entity.SubscribeAudit;
import vip.xiaozhao.intern.baseUtil.intf.mapper.BookShelfMapper;
import vip.xiaozhao.intern.baseUtil.intf.mapper.MessageMapper;
import vip.xiaozhao.intern.baseUtil.intf.service.MessageService;
import vip.xiaozhao.intern.baseUtil.intf.utils.MessageUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author hlxllo
 * @description 消息业务层实现
 * @date 2025/2/8
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private BookShelfMapper bookShelfMapper;

    @Resource
    private MessageMapper messageMapper;

    // 每天凌晨2点执行
    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateMessageLevel() {
        // 获取所有书架（订阅）信息
        List<Bookshelf> shelves = bookShelfMapper.getAllBookshelves();
        // 获取所有用户最近三次阅读记录
        for (Bookshelf shelf : shelves) {
            int userId = shelf.getUserId();
            int novelId = shelf.getNovelId();
            List<SubscribeAudit> last3Audits = bookShelfMapper.getLastSubscribeAuditsByUserIdAndNovelId(userId, novelId, 3);
            // 如果用户订阅了小说但还没看过，则给默认值（最高级）
            int level = 1;
            if (!CollUtil.isEmpty(last3Audits)) {
                Instant now = Instant.now();
                // 取后三条记录的最低等级（数字最大的）
                int highestLevel = last3Audits.stream()
                        .sorted(Comparator.comparingInt(SubscribeAudit::getLevel).reversed()).toList().get(0).getLevel();
                // 获取最后一次阅读时间
                Date lastReadTime = last3Audits.get(0).getAddTime();
                long lastReadGap = Duration.between(lastReadTime.toInstant(), now).toMinutes();
                int lastReadLevel = MessageUtil.getLevel(lastReadGap);
                // 获取最后一次发送通知时间
                Message message = messageMapper.getLastMessage(userId, novelId);
                int lastMessageLevel = 1;
                if (!ObjUtil.isEmpty(message)) {
                    Date lastMessageTime = message.getSendTime();
                    long lastMessageGap = Duration.between(lastMessageTime.toInstant(), now).toMinutes();
                    lastMessageLevel = MessageUtil.getLevel(lastMessageGap);
                }
                // 决定最终等级（三者最大值，定时任务只用于降级）
                level = Math.max(highestLevel, Math.max(lastReadLevel, lastMessageLevel));
            }
            // 更新用户等级
            messageMapper.upgradeLevel(userId, novelId, level);
        }
    }
}
