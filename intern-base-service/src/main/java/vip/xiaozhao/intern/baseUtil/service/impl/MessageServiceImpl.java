package vip.xiaozhao.intern.baseUtil.service.impl;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vip.xiaozhao.intern.baseUtil.intf.entity.Bookshelf;
import vip.xiaozhao.intern.baseUtil.intf.entity.SubscribeAudit;
import vip.xiaozhao.intern.baseUtil.intf.mapper.BookShelfMapper;
import vip.xiaozhao.intern.baseUtil.intf.mapper.MessageMapper;
import vip.xiaozhao.intern.baseUtil.intf.service.MessageService;

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

            }
            // 更新用户等级
            messageMapper.upgradeLevel(userId, novelId, level);
        }
    }
}
