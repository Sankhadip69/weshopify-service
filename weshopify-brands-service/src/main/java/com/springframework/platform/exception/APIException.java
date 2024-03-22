package com.springframework.platform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;

@Getter
@AllArgsConstructor
public class APIException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 2396464218255008420L;

    private String errorMsg;
    private int errorCode;
}
