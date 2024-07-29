package com.flab.tess.util;

import com.flab.tess.dto.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler
    public ErrorResponse handleException(IllegalArgumentException e){
        return new ErrorResponse(e.getMessage());
    }

}
