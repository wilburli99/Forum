package cn.iocoder.forum.exception;

import cn.iocoder.forum.common.AppResult;
import cn.iocoder.forum.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/*
  * 统一异常处理
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理⾃定义的已知异常
     * @param e ApplicationException
     * @return AppResult
     */
    @ResponseBody
    @ExceptionHandler(ApplicationException.class)
    public AppResult applicationExceptionHandler(ApplicationException e) {
        // 打印异常
        e.printStackTrace();
        // 记录日志
        log.error(e.getMessage());
        if (e.getErrorResult() != null) {
            return e.getErrorResult();
        }
        // 非空校验
        if (e.getMessage() == null || e.getMessage().equals("")) {
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        // 默认返回失败
        return AppResult.failed(e.getMessage());
    }

    /**
     * 处理未捕获的其他未知异常
     * @param e Exception
     * @return AppResult
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AppResult exceptionHandler(Exception e) {
        // 打印异常
        e.printStackTrace();
        // 打印日志
        log.error(e.getMessage());
        if (e.getMessage() == null || e.getMessage().equals("")) {
            return AppResult.failed(ResultCode.ERROR_SERVICES);
        }
        return AppResult.failed(e.getMessage());
    }
}
