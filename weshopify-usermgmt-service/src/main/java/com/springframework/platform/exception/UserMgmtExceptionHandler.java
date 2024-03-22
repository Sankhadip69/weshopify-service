package com.springframework.platform.exception;

import com.springframework.platform.dto.ErrorList;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
public class UserMgmtExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<Object> handleApiException(APIException apiException) {
        UserException userException = UserException.builder().message(apiException.getErrorMsg())
                .statusCode(apiException.getErrorCode()).timeStamp(new Date()).build();
        return ResponseEntity.badRequest().body(userException);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                    WebRequest request) {
        List<UserException> userExceptionList = new ArrayList<>();

        ex.getBindingResult().getAllErrors().stream().forEach(objectError -> {
            UserException userException = UserException.builder().message(objectError.getDefaultMessage())
                    .statusCode(status.value()).timeStamp(new Date()).build();
            userExceptionList.add(userException);
        });
        ErrorList errorList = ErrorList.builder().messages(userExceptionList).build();

        return ResponseEntity.badRequest().body(errorList);
    }
}
