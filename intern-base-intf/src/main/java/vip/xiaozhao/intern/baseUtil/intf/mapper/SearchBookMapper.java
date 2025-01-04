package vip.xiaozhao.intern.baseUtil.intf.mapper;

import vip.xiaozhao.intern.baseUtil.intf.DO.HotNovelDO;
import vip.xiaozhao.intern.baseUtil.intf.DO.NovelDO;
import vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly;

import java.util.List;

public interface SearchBookMapper {

    @ReadOnly
    public List<NovelDO> searchNovelList(int start,int pageSize,String query);

    public void incrementNovelSearchNum(int novellId);

    public void inserIntoHotBook(int id,String bookName);

    @ReadOnly
    public HotNovelDO getHotNovelByNovelId(int novelId);

    @ReadOnly
    public List<HotNovelDO> getHotNovelList();

}
