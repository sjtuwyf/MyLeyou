package com.leyou.commom.advice;

import com.leyou.commom.exception.LyException;
import com.leyou.commom.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author ssqswyf
 * @Date 2021/4/18
 */
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handlerException(LyException e) {
        return ResponseEntity.status(e.getExceptionEnum().getCode()).
                body(new ExceptionResult(e.getExceptionEnum()));
    }
}
