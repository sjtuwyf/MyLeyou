package com.leyou.commom.vo;

import com.leyou.commom.enums.ExceptionEnum;
import lombok.Data;

/**
 * @Author ssqswyf
 * @Date 2021/4/19
 */
@Data
public class ExceptionResult {
    private int status;
    private String message;
    private Long timestamp;

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
