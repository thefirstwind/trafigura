package com.trafigura.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.trafigura.common.vo.REQ;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(annotations={RestController.class,Controller.class})
public class BizExceptionHandler {

    /**
     * 捕获非业务异常
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    REQ exceptionHandler(Exception e) {
        log.error("error={}",e);
        return REQ.error("系统异常，请联系客服");
    }

    /**
     * 捕获业务异常
     */
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    REQ businessExceptionHandler(IllegalArgumentException e) {
        log.error("业务异常error:{}",e);
        return REQ.error(e.getMessage());
    }

}
