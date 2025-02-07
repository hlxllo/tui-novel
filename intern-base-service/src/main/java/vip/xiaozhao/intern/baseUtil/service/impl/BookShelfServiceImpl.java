package vip.xiaozhao.intern.baseUtil.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaozhao.intern.baseUtil.intf.constant.RedisConstant;
import vip.xiaozhao.intern.baseUtil.intf.entity.Message;
import vip.xiaozhao.intern.baseUtil.intf.entity.NovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.entity.Bookshelf;
import vip.xiaozhao.intern.baseUtil.intf.mapper.BookShelfMapper;
import vip.xiaozhao.intern.baseUtil.intf.mapper.MessageMapper;
import vip.xiaozhao.intern.baseUtil.intf.service.BookShelfService;
import vip.xiaozhao.intern.baseUtil.intf.service.NovelInfoService;
import vip.xiaozhao.intern.baseUtil.intf.utils.ConvertUtils;
import vip.xiaozhao.intern.baseUtil.intf.utils.MessageUtil;
import vip.xiaozhao.intern.baseUtil.intf.utils.redis.RedisUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BookShelfServiceImpl implements BookShelfService {

    private static final int BATCH_SIZE = 100;

    @Resource
    private BookShelfMapper bookShelfMapper;

    @Resource
    private NovelInfoService novelInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private MessageMapper messageMapper;


    @Override
    public List<Bookshelf> getBookShelfByUserId(int userId) throws Exception {
        String key = RedisConstant.PRE_USER_ID + userId;
        Object o = redisTemplate.opsForValue().get(key);
        List<Bookshelf> bookShelf;
        if (o == null) {
            bookShelf = bookShelfMapper.getBookShelfByUserId(userId);
            if (bookShelf == null) {
                throw new RuntimeException("用户不存在");
            }
            // 顶置排序
            bookShelf.sort((o1, o2) -> o2.getIsTop() - o1.getIsTop());
            redisTemplate.opsForValue().set(key, bookShelf, RedisUtils.EXRP_ONE_HOUR, TimeUnit.SECONDS);
        } else {
            bookShelf = ConvertUtils.convert2List(o, Bookshelf.class);
        }
        return bookShelf;
    }


    @Override
    public void readChapter(int userId, int novelId, int chapterId) throws Exception {
        // 先判断是否订阅
        List<Bookshelf> bookshelves = bookShelfMapper.getBookShelfByUserId(userId);
        if (CollUtil.isEmpty(bookshelves)) {
            throw new RuntimeException("用户没有订阅该小说");
        }
        bookshelves = bookshelves.stream().filter(b -> b.getNovelId() == novelId).toList();
        if (CollUtil.isEmpty(bookshelves)) {
            throw new RuntimeException("用户没有订阅该小说");
        }
        Bookshelf bookshelf = bookshelves.get(0);
        if (ObjUtil.isEmpty(bookshelf)) {
            throw new RuntimeException("用户没有订阅该小说");
        }
        // 获取小说最新更新时间
        Date lastUpdateTime = bookshelf.getLastUpdateTime();
        // 小说从未更新过，说明用户主动，提升等级
        if (ObjUtil.isEmpty(lastUpdateTime)) {
            lastUpdateTime = new Date();
        }
        // 获取最后一次发送消息时间
        Message message = messageMapper.getLastMessage(userId, novelId, chapterId);
        Date lastSendTime;
        // 从未发送过消息，说明用户主动，应该提升等级
        if (ObjUtil.isEmpty(message)) {
            lastSendTime = new Date();
        } else {
            lastSendTime = message.getSendTime();
        }
        // 计算到当前时间的差值
        Instant now = Instant.now();
        long updateGap = Duration.between(lastUpdateTime.toInstant(), now).toMinutes();
        long sendGap = Duration.between(lastSendTime.toInstant(), now).toMinutes();
        // 获取最小差值进行等级变更
        long minGap = Math.min(updateGap, sendGap);
        int level = MessageUtil.getLevel(minGap);
        // 获取发送等级
        Integer messageLevel = messageMapper.getMessageLevel(userId, novelId);
        if (messageLevel == null) {
            messageLevel = 0;
        }
        // 等级 345 且本次比之前等级高的用户升一级
        if (messageLevel >= 3 && messageLevel > level) {
            messageMapper.upgradeLevel(userId, novelId, messageLevel - 1);
        }
        // 向流水表中插入数据
        bookShelfMapper.readChapter(userId, novelId, chapterId, (int) minGap);
        //List<SubscribeAudit> subscribeAudit = bookShelfMapper.getLastSubscribeAuditsByUserIdAndNovelId(userId, novelId);
        //if (!CollUtil.isEmpty(subscribeAudit)) {
        //    bookShelfMapper.updateSubscribeAuditChapterId(userId, novelId, chapterId);
        //} else {
        //    bookShelfMapper.readChapter(userId, novelId, chapterId);
        //}
        // 删除缓存防止脏读
        redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);
    }


    @Override
    public void updateTopBook(int userId, int novelId) throws Exception {
        List<Bookshelf> books = getBookShelfByUserId(userId);
        // 是否订阅了这本书
        // 获取所有的小说 id
        List<Integer> ids = books.stream().map(Bookshelf::getNovelId).toList();
        if (!ids.contains(novelId)) {
            throw new RuntimeException("用户没有订阅该小说");
        }
        int isTopId = -1;
        for (Bookshelf book : books) {
            if (book.getIsTop() == 1) {
                isTopId = book.getNovelId();
                break;
            }
        }
        // 只有一本书可以省一次
        if (isTopId != -1 && books.size() > 1) {
            bookShelfMapper.updateTopBook(userId, isTopId);
        }
        bookShelfMapper.updateTopBook(userId, novelId);
        // 删除缓存防止脏读
        redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);
    }

    @Override
    public void deleteBookByUserIdAndNovelId(int userId, int novelId) throws Exception {
        getBookShelfByUserId(userId);
        bookShelfMapper.deleteBookByUserIdAndNovelId(userId, novelId);
        // 删除缓存防止脏读
        redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);

        RedisUtils.deleteUserIdFromNovelId(userId, novelId);

    }

    @Override
    @Transactional
    public void subscribeNovel(int userId, int novelId) throws Exception {
        List<Bookshelf> books = getBookShelfByUserId(userId);
        if (books.size() >= 5) {
            throw new RuntimeException("小说已经满5本，不可以继续添加");
        }
        NovelInfo novelInfo = novelInfoService.getNovelInfoByNovelId(novelId);
        Bookshelf bookshelf = getBookshelf(userId, novelId, novelInfo);
        bookShelfMapper.subscribeBook(bookshelf);
        // 如果是第一次订阅，新增消息等级表
        int level = messageMapper.getMessageLevel(userId, novelId);
        if (level == 0) {
            // 默认为最高级
            messageMapper.insertMessageLevel(userId, novelId);
        }
        // 删除缓存防止脏读
        redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);

        String key = RedisConstant.PRE_NOVEL_ID + novelId;
        // 使用 Redis Set 存储用户 Id
        try {
            RedisUtils.addToSet(key, userId, RedisUtils.EXRP_ONE_HOUR, true);
            System.out.println("用户 Id: " + userId + " 已添加到书籍: " + novelId + " 的用户列表中");
        } catch (Exception e) {
            throw new RuntimeException("添加用户 Id 到 Redis Set 时出现异常", e);
        }

    }


    @Override
    public void updateIsReadByUserIdAndNovelId(int userId, int novelId) throws Exception {
        bookShelfMapper.updateIsReadByUserIdAndNovelId(userId, novelId);
        // 删除缓存防止脏读
        redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);
    }


    @Override
    public void updateIsReadByNovelId(int novelId) throws Exception {
        // redis 批量更新
        Set<String> setMembers = RedisUtils.getSetMembers(RedisConstant.PRE_NOVEL_ID + novelId);
        // 没人订阅，不需要更新
        if (CollUtil.isEmpty(setMembers)) {
            return;
        }
        List<Integer> userIds = setMembers.stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        int totalSize = userIds.size();
        for (int i = 0; i < totalSize; i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, totalSize);
            List<Integer> batchUserIds = userIds.subList(i, end);
            bookShelfMapper.updateIsReadByNovelId(novelId, batchUserIds);
        }

        for (int i = 0; i < totalSize; i++) {
            int userId = userIds.get(i);
            // 删除缓存防止脏读
            redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);
        }
         /*
         或者使用多线程处理
        // 批处理大小
        final int batchSize = 100; // 根据需要调整
        int totalSize = userIDs.size();

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(10); // 根据需要调整线程数
        CountDownLatch latch = new CountDownLatch((totalSize + batchSize - 1) / batchSize); // 计算总的批次数

        // 分批处理
        for (int i = 0; i < totalSize; i += batchSize) {
            final List<Integer> batchUserIDs = userIDs.subList(i, Math.min(i + batchSize, totalSize));

            // 提交任务到线程池
            executor.submit(() -> {
                try {
                    // 数据库更新操作
                    bookShelfMapper.updateIsReadByNovelId(NovelId, batchUserIDs);
                } finally {
                    latch.countDown(); // 完成该批次后减少计数
                }
            });
        }

        // 等待所有任务完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace(); // 处理异常
        } finally {
            executor.shutdown(); // 关闭线程池
        }

          */

    }

    @Override
    public void updateIsReadByNovelIdList(List<Integer> novelIds) throws Exception {
        for (Integer novelId : novelIds) {
            // redis 批量更新
            Set<String> setMembers = RedisUtils.getSetMembers(RedisConstant.PRE_NOVEL_ID + novelId);
            if (CollUtil.isEmpty(setMembers)) {
                return;
            }
            List<Integer> userIds = setMembers.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            int totalSize = userIds.size();

            for (int i = 0; i < totalSize; i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, totalSize);
                List<Integer> batchUserIds = userIds.subList(i, end);
                bookShelfMapper.updateIsReadByNovelId(novelId, batchUserIds);
            }
            for (int i = 0; i < totalSize; i++) {
                int userId = userIds.get(i);
                // 删除缓存防止脏读
                redisTemplate.delete(RedisConstant.PRE_USER_ID + userId);
            }
        }

    }


    @NotNull
    private static Bookshelf getBookshelf(int userId, int novelId, NovelInfo novelInfo) {
        Bookshelf bookshelf = new Bookshelf();
        bookshelf.setUserId(userId);
        bookshelf.setNovelId(novelId);
        bookshelf.setBookName(novelInfo.getBookName());
        bookshelf.setCoverUrl(novelInfo.getCover());
        bookshelf.setAuthorName(novelInfo.getAuthorName());
        bookshelf.setLastUpdateTime(novelInfo.getLastUpdateTime());
        bookshelf.setLatestChapterId(novelInfo.getLatestChapterId());
        bookshelf.setLatestChapter(novelInfo.getLatestChapter());
        return bookshelf;
    }

}
