package com.leyou.commom.exception;

import com.leyou.commom.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author ssqswyf
 * @Date 2021/4/19
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LyException extends RuntimeException{

    private ExceptionEnum exceptionEnum;


}
