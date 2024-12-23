package vip.xiaozhao.intern.baseUtil.intf.utils.uploadUtils;

import jakarta.annotation.Resource;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadUtils {

    @Resource
    private FileStorageService fileStorageService;

    public String upload(MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
                .setPath("upload/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setSaveFilename("image.jpg") //设置保存的文件名，不需要可以不写，会随机生成
                //.setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                //.setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                //.putAttr("role", "admin") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }
}
