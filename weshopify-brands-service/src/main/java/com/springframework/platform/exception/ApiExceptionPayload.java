package com.springframework.platform.exception;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class ApiExceptionPayload implements Serializable {


    @Serial
    private static final long serialVersionUID = 6885614892172816010L;

    private String message;
    private int statusCode;
    private Date timeStamp;
}
