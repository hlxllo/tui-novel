package vip.xiaozhao.intern.baseUtil.controller.sts;

import com.aliyun.sts20150401.Client;
import com.aliyun.sts20150401.models.AssumeRoleRequest;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaozhao.intern.baseUtil.config.StsProperties;
import vip.xiaozhao.intern.baseUtil.intf.dto.ResponseDO;


@RestController
@RequestMapping("/wuyan")
@Slf4j
public class StsController {

    @Resource
    private StsProperties stsProperties;

    @Resource
    private Client stsClient;

    @GetMapping("/sts")
    public ResponseDO generateStsToken() {
        AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest()
            .setDurationSeconds(900L)
            // 将<your_role_session_name>设置为自定义的会话名称，例如 my-website-server。
            .setRoleSessionName(stsProperties.getRoleSessionName())
            // 将<your_role_arn>替换为拥有上传文件到指定OSS Bucket权限的RAM角色的ARN，可以在 RAM 角色详情中获得角色 ARN，详情参考：https://help.aliyun.com/zh/ram/user-guide/view-the-information-about-a-ram-role
            .setRoleArn(stsProperties.getRoleArn());
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            AssumeRoleResponse response = stsClient.assumeRoleWithOptions(assumeRoleRequest, runtime);
            return ResponseDO.success(response.body.credentials);
        } catch (Exception error) {
            // 如有需要，请打印 error
            log.error("{}", error.getMessage());
            return ResponseDO.fail(error.getMessage());
        }
    }
}
