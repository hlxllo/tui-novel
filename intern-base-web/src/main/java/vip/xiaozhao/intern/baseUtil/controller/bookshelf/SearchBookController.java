package vip.xiaozhao.intern.baseUtil.controller.bookshelf;

import cn.hutool.core.util.ObjUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import vip.xiaozhao.intern.baseUtil.controller.BaseController;
import vip.xiaozhao.intern.baseUtil.intf.constant.PageConstant;
import vip.xiaozhao.intern.baseUtil.intf.constant.RedisConstant;
import vip.xiaozhao.intern.baseUtil.intf.dto.ResponseDO;
import vip.xiaozhao.intern.baseUtil.intf.entity.HotNovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.entity.NovelInfo;
import vip.xiaozhao.intern.baseUtil.intf.service.NovelInfoService;
import vip.xiaozhao.intern.baseUtil.intf.service.SearchBookService;
import vip.xiaozhao.intern.baseUtil.intf.utils.ConvertUtils;
import vip.xiaozhao.intern.baseUtil.intf.utils.redis.RedisUtils;
import vip.xiaozhao.intern.baseUtil.intf.vo.NovelBasicInfoVo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/tuitui3/searchBook")
public class SearchBookController extends BaseController {

    @Resource
    private SearchBookService searchBookService;

    @Resource
    private NovelInfoService novelInfoService;

    @Resource
    private RedisTemplate redisTemplate;

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
    public ResponseDO incrementNovelSearchNum(@PathVariable int novelId) {
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
        Object o = redisTemplate.opsForValue().get(RedisConstant.HOT_NOVEL_LIST);
        // 缓存里有直接返回
        if (!ObjUtil.isEmpty(o)) {
            List<HotNovelInfo> hotNovels = ConvertUtils.convert2List(o, HotNovelInfo.class);
            return ResponseDO.success(hotNovels);
        }
        // 没有再去查表
        List<HotNovelInfo> hotNovelList = searchBookService.getHotNovelList();
        redisTemplate.opsForValue().set(RedisConstant.HOT_NOVEL_LIST, hotNovelList, RedisUtils.EXRP_ONE_HOUR, TimeUnit.SECONDS);
        Collections.shuffle(hotNovelList);
        List<HotNovelInfo> vos = new ArrayList<>();
        int count = Math.min(10, hotNovelList.size());
        for (int i = 0; i < count; i++) {
            vos.add(hotNovelList.get(i));
        }
        return ResponseDO.success(vos);
    }


}
