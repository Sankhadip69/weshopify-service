package com.springframework.platform.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class BrandsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Object> handleApiException(APIException apiException) {
        ApiExceptionPayload userException = ApiExceptionPayload.builder().message(apiException.getErrorMsg())
                .statusCode(apiException.getErrorCode()).timeStamp(new Date()).build();
        return ResponseEntity.badRequest().body(userException);
    }


}
