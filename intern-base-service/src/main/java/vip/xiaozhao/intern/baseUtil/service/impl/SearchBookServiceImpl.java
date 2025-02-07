package vip.xiaozhao.intern.baseUtil.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.xiaozhao.intern.baseUtil.intf.entity.HotNovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.mapper.SearchBookMapper;
import vip.xiaozhao.intern.baseUtil.intf.service.NovelInfoService;
import vip.xiaozhao.intern.baseUtil.intf.service.SearchBookService;
import vip.xiaozhao.intern.baseUtil.intf.vo.NovelBasicInfoVo;

import java.util.List;

@Service
public class SearchBookServiceImpl implements SearchBookService {

    @Resource
    private SearchBookMapper searchBookMapper;

    @Override
    public List<NovelBasicInfoVo> searchNovelBasicList(int start, int pageSize, String query) {

        return searchBookMapper.searchNovelBasicList(start, pageSize, query);
    }

    @Override
    public void insertHotBook(int id, String bookName) {
        searchBookMapper.insertHotBook(id, bookName);
    }

    @Override
    public HotNovelInfo getHotNovelByNovelId(int novelId) {
        return searchBookMapper.getHotNovelByNovelId(novelId);
    }


    @Override
    public void incrementNovelSearchNum(int novelId) {
        searchBookMapper.incrementNovelSearchNum(novelId);
    }

    @Override
    public List<HotNovelInfo> getHotNovelList() {
        List<HotNovelInfo> hotNovelList = searchBookMapper.getHotNovelList();
        return hotNovelList;
    }

}
