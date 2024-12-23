package vip.xiaozhao.intern.baseUtil.controller.upload;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaozhao.intern.baseUtil.controller.BaseController;
import vip.xiaozhao.intern.baseUtil.intf.dto.ResponseDO;
import vip.xiaozhao.intern.baseUtil.intf.service.ImageService;
import vip.xiaozhao.intern.baseUtil.intf.utils.uploadUtils.Base64ImageUtils;
import vip.xiaozhao.intern.baseUtil.intf.utils.uploadUtils.UploadUtils;

// 通用上传请求
@RestController
@RequestMapping("wuyan/upload")
public class UploadController extends BaseController {
    @Override
    protected int getCurrentUserId(HttpServletRequest request) {
        return super.getCurrentUserId(request);
    }

    @Resource
    private UploadUtils uploadUtils;

    @Resource
    private ImageService imageService;

    // 图片上传
    @PostMapping
    public ResponseDO upload(String image) {
        // 解码图片
        MultipartFile file = Base64ImageUtils.decodeToMultipartFile(image);
        // 上传
        String url = uploadUtils.upload(file);
        if (url.equals("上传失败！")) {
            return ResponseDO.fail("上传失败！");
        }
        imageService.insertImage(url);
        return ResponseDO.success(url);
    }
}
