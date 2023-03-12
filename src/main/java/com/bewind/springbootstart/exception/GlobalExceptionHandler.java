package com.bewind.springbootstart.exception;

import com.bewind.springbootstart.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获自定义异常
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public R<Map<String, Object>> handle(ApiException e) {
        log.error("ApiException", e);
        return R.failed(e.getMessage());
    }
}
