package vip.xiaozhao.intern.baseUtil.controller.bookshelf;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import vip.xiaozhao.intern.baseUtil.controller.BaseController;
import vip.xiaozhao.intern.baseUtil.intf.dto.ResponseDO;
import vip.xiaozhao.intern.baseUtil.intf.entity.Bookshelf;
import vip.xiaozhao.intern.baseUtil.intf.service.BookShelfService;

import java.util.List;

@RestController
@RequestMapping("/tuitui3/bookshelf")
public class BookShelfController extends BaseController {

    @Resource
    private BookShelfService bookShelfService;


    // 获取用户书架
    @GetMapping("/getBookShelfByUserId")
    public ResponseDO getBookShelfByUserId(HttpServletRequest request) throws Exception {
        int userId = getCurrentUserId(request);
        if (userId == -1) {
            return ResponseDO.fail("您还未登录");
        }
        List<Bookshelf> bookShelf = bookShelfService.getBookShelfByUserId(userId);
        return ResponseDO.success(bookShelf);
    }

    // 用户阅读章节
    @PostMapping("/readChapter")
    public ResponseDO readChapter(HttpServletRequest request,
                                  @RequestParam(value = "novelId", defaultValue = "-1") int novelId,
                                  @RequestParam(value = "chapterId", defaultValue = "-1") int chapterId) throws Exception {
        // 这里是用户读哪一章记录一下
        int userId = getCurrentUserId(request);
        if (userId == -1) {
            return ResponseDO.fail("您还未登录");
        }
        System.err.println(chapterId);
        if (chapterId == -1 || novelId == -1) {
            return ResponseDO.fail("参数错误");
        }
        bookShelfService.readChapter(userId, novelId, chapterId);
        return ResponseDO.success(null);
    }

    // 用户置顶/非置顶小说
    @PutMapping("/updateTopBook/{novelId}")
    public ResponseDO updateTopBook(HttpServletRequest request, @PathVariable int novelId) throws Exception {
        int userId = getCurrentUserId(request);
        if (userId == -1) {
            return ResponseDO.fail("您还未登录");
        }
        bookShelfService.updateTopBook(userId, novelId);
        return ResponseDO.success(null);
    }

    // 用户删除小说
    // 同时也是取消订阅
    @DeleteMapping("/delete/{novelId}")
    public ResponseDO deleteBook(HttpServletRequest request, @PathVariable int novelId) throws Exception {
        int userId = getCurrentUserId(request);
        if (userId == -1) {
            return ResponseDO.fail("您还未登录");
        }
        bookShelfService.deleteBookByUserIdAndNovelId(userId, novelId);
        return ResponseDO.success(null);
    }

    // 订阅小说
    @PostMapping("/subscribe/{novelId}")
    public ResponseDO subscribeNovel(HttpServletRequest request, @PathVariable int novelId) throws Exception {
        int userId = getCurrentUserId(request);
        if (userId == -1) {
            return ResponseDO.fail("您还未登录");
        }
        bookShelfService.subscribeNovel(userId, novelId);
        return ResponseDO.success(null);
    }

    // 这里是用户点开阅读了变为已读
    @PutMapping("/updateUserIsRead")
    public ResponseDO updateUserIsRead(HttpServletRequest request,
                                       @RequestParam(value = "novelId", defaultValue = "-1") int novelId) throws Exception {
        int userId = getCurrentUserId(request);
        if (userId == -1) {
            return ResponseDO.fail("您还未登录");
        }
        if (novelId == -1) {
            return ResponseDO.fail("参数错误");
        }
        bookShelfService.updateIsReadByUserIdAndNovelId(userId, novelId);
        return ResponseDO.success(null);
    }

    // 将这本小说对应的用户改为未读,这个是根据一本小说id进行修改用户未读状态
    @PutMapping("/updateUserUnRead/{novelId}")
    public ResponseDO updateUserUnRead(HttpServletRequest request, @PathVariable int novelId) throws Exception {
        //int userId = getCurrentUserId(request);
        //if (userId == -1) {
        //    return ResponseDO.fail("您还未登录");
        //}
        bookShelfService.updateIsReadByNovelId(novelId);
        return ResponseDO.success(null);
    }

    // 这个是根据传过来的一组小说id来修改用户的未读状态
    @PutMapping("/updateUserUnReadBatch")
    public ResponseDO updateUserUnReadBatch(HttpServletRequest request,
                                            @RequestBody List<Integer> novelIds) throws Exception {
        //int userId = getCurrentUserId(request);
        //if (userId == -1) {
        //    return ResponseDO.fail("您还未登录");
        //}
        bookShelfService.updateIsReadByNovelIdList(novelIds);
        return ResponseDO.success(null);
    }


}
