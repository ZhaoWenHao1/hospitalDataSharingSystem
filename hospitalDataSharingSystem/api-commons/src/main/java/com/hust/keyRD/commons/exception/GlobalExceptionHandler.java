package com.hust.keyRD.commons.exception;

import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.exception.fabric.FabricException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @program: fabricTest
 * @description: 全局异常处理
 * @author: zwh
 * @create: 2021-01-08 09:54
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FabricException.class)
    public CommonResult handleFabricException(FabricException e) {
        return new CommonResult<>(400, "fabric error: " + e.getMessage());
        //return "fabric error: " + e.getMessage();
    }
    
    @ExceptionHandler(BadRequestException.class)
    public CommonResult handleBadRequestException(BadRequestException e){
        return new CommonResult<>(400, "bad request: " + e.getMessage());
    }


}
