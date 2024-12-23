package vip.xiaozhao.intern.baseUtil.intf.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vip.xiaozhao.intern.baseUtil.intf.dto.ResponseDO;


/*全局异常处理器*/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //@ExceptionHandler
    public ResponseDO baseExceptionHandler(Exception e) {
        log.error("异常信息:{}", e.getMessage());
        // 默认状态码默认都500
        return ResponseDO.fail(e.getMessage());
    }

}
