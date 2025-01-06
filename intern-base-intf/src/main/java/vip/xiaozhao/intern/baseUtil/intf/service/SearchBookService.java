package vip.xiaozhao.intern.baseUtil.intf.service;

import vip.xiaozhao.intern.baseUtil.intf.entity.HotNovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.vo.NovelBasicInfoVo;

import java.util.List;

public interface SearchBookService {

    List<NovelBasicInfoVo> searchNovelBasicList(int start, int pageSize, String query);

    void incrementNovelSearchNum(int novelId);

    void insertHotBook(int id, String bookName);

    HotNovelInfo getHotNovelByNovelId(int novelId);

    List<HotNovelInfo> getHotNovelList();

}
