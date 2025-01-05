package vip.xiaozhao.intern.baseUtil.intf.service;

import vip.xiaozhao.intern.baseUtil.intf.entity.Bookshelf;

import java.util.List;

public interface BookShelfService {

    List<Bookshelf> getBookShelfByUserId(int userId) throws Exception;

    void readChapter(int userId, int novelId, int chapterId) throws Exception;

    void updateTopBook(int userId, int novelId) throws Exception;

    void deleteBookByUserIdAndNovelId(int userId, int novelId) throws Exception;

    void subscribeNovel(int userId, int novelId) throws Exception;

    void updateIsReadByUserIdAndNovelId(int userId, int novelId) throws Exception;

    void updateIsReadByNovelId(int novelId) throws Exception;

    void updateIsReadByNovelIdList(List<Integer> novelIds) throws Exception;


}
