package vip.xiaozhao.intern.baseUtil.intf.utils.uploadUtils;

import org.springframework.web.multipart.MultipartFile;
import vip.xiaozhao.intern.baseUtil.intf.entity.Base64MultipartFile;

public class Base64ImageUtils {

    /**
     * 将 Base64 编码的图片解码为 MultipartFile
     *
     * @param base64Image Base64 编码的图片字符串
     * @return MultipartFile 对象
     */
    public static MultipartFile decodeToMultipartFile(String base64Image) {
        return new Base64MultipartFile(base64Image);
    }
}