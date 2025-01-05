package vip.xiaozhao.intern.baseUtil.controller.bookshelf;

import cn.hutool.core.util.ObjUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import vip.xiaozhao.intern.baseUtil.controller.BaseController;
import vip.xiaozhao.intern.baseUtil.intf.DO.HotNovelDO;
import vip.xiaozhao.intern.baseUtil.intf.constant.PageConstant;
import vip.xiaozhao.intern.baseUtil.intf.constant.RedisConstant;
import vip.xiaozhao.intern.baseUtil.intf.dto.ResponseDO;
import vip.xiaozhao.intern.baseUtil.intf.entity.HotNovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.entity.NovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.service.NovelInfoService;
import vip.xiaozhao.intern.baseUtil.intf.service.SearchBookService;
import vip.xiaozhao.intern.baseUtil.intf.utils.redis.RedisUtils;
import vip.xiaozhao.intern.baseUtil.intf.vo.NovelBasicInfoVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/tuitui3/searchBook")
public class SearchBookController extends BaseController {

    @Resource
    private SearchBookService searchBookService;

    @Resource
    private NovelInfoService novelInfoService;

    // 全文检索小说（书名和作者）
    @GetMapping("/searchNovelList")
    public ResponseDO searchNovelList(@RequestParam(name = "page") int page,
                                      @RequestParam(name = "query") String query) {
        if (page < 1 || query.trim().isEmpty()) {
            return ResponseDO.fail("参数错误");
        }
        int start = (page - 1) * PageConstant.PAGE_SIZE;
        List<NovelBasicInfoVo> vos = searchBookService.searchNovelBasicList(start, PageConstant.PAGE_SIZE, query);
        return ResponseDO.success(vos);
    }

    // 增加搜索次数
    @PostMapping("/incrementNovelSearchNum/{novelId}")
    public ResponseDO incrementNovelSearchNum(@PathVariable(required = true) int novelId) {
        if (novelId < 1) {
            return ResponseDO.fail("参数错误");
        }
        HotNovelInfo hotNovelInfo = searchBookService.getHotNovelByNovelId(novelId);
        // 热榜里没有，去主表查询
        if (hotNovelInfo == null) {
            NovelInfo novelInfo = novelInfoService.getNovelInfoByNovelId(novelId);
            if (ObjUtil.isEmpty(novelInfo)) {
                return ResponseDO.fail("小说不存在");
            }
            int id = novelInfo.getId();
            String bookName = novelInfo.getBookName();
            searchBookService.insertHotBook(id, bookName);
        } else {
            searchBookService.incrementNovelSearchNum(novelId);
        }
        return ResponseDO.success(null);
    }


    // TODO 10天的还没搞定
    // 获取热榜小说（10本）
    @GetMapping("/getHotNovelList")
    public ResponseDO getHotNovelList() {
        String redisHotNovelList = RedisUtils.get(RedisConstant.HOT_NOVEL_LIST);
        if (redisHotNovelList != null) {
            return ResponseDO.success(redisHotNovelList);
        }
        List<HotNovelDO> hotNovelList = searchBookService.getHotNovelList();
        RedisUtils.set(RedisConstant.HOT_NOVEL_LIST, hotNovelList, RedisUtils.EXRP_ONE_HOUR);
        if (hotNovelList.isEmpty()) {
            return ResponseDO.success(null);
        }
        Collections.shuffle(hotNovelList);
        List<HotNovelDO> randomHotNovels = new ArrayList<>();
        int count = Math.min(10, hotNovelList.size());
        for (int i = 0; i < count; i++) {
            randomHotNovels.add(hotNovelList.get(i));
        }
        return ResponseDO.success(randomHotNovels);
    }


}
