package com.springframework.platform.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class UserAuthBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -7783192644031037096L;

    private String userName;
    private String password;
}
