package com.springframework.platform.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class WSO2UserPersonals implements Serializable {

    @Serial
    private static final long serialVersionUID = 5157254763218000127L;

    //first name
    private String givenName;
    //last name
    private String familyName;

}
