package vip.xiaozhao.intern.baseUtil.intf.mapper;

import vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly;
import vip.xiaozhao.intern.baseUtil.intf.entity.HotNovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.vo.NovelBasicInfoVo;

import java.util.List;

public interface SearchBookMapper {

    @ReadOnly
    List<NovelBasicInfoVo> searchNovelBasicList(int start, int pageSize, String query);

    void incrementNovelSearchNum(int novellId);

    void insertHotBook(int id, String bookName);

    @ReadOnly
    HotNovelInfo getHotNovelByNovelId(int novelId);

    @ReadOnly
    List<HotNovelInfo> getHotNovelList();

}
