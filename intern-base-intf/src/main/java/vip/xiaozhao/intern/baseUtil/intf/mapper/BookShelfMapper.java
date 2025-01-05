package vip.xiaozhao.intern.baseUtil.intf.mapper;

import org.apache.ibatis.annotations.Mapper;
import vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly;
import vip.xiaozhao.intern.baseUtil.intf.entity.Bookshelf;
import vip.xiaozhao.intern.baseUtil.intf.entity.SubscribeAudit;

import java.util.List;

@Mapper
public interface BookShelfMapper {

    @ReadOnly
    List<Bookshelf> getBookShelfByUserId(int userId);

    void readChapter(int userId, int novelId, int chapterId);

    void updateTopBook(int userId, int novelId);

    void deleteBookByUserIdAndNovelId(int userId, int novelId);

    void subscribeBook(Bookshelf bookshelf);

    void updateIsReadByUserIdAndNovelId(int userId, int novelId);

    void updateIsReadByNovelId(int novelId, List<Integer> userIds);

    @ReadOnly
    List<SubscribeAudit> getSubscribeAuditByUserIdAndNovelId(int userId, int novelId);

    void updateSubscribeAuditChapterId(int userId, int novelId, int chapterId);


}
