package com.springframework.platform.bean;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Jacksonized annotation is resolving the conflict
 * happening to the default constrcutor with the
 * @Builder
 */
@Jacksonized
@Data
@Builder
public class UserBean implements Serializable {
    @Serial
    private static final long serialVersionUID = -65940987159153750L;

    private String id;

    @NotEmpty(message = "first name is mandatory. it should not be empty")
    private String firstName;

    @NotEmpty(message = "last name is mandatory. it should not be empty")
    private String lastName;

    @NotEmpty(message = "email name is mandatory. please enter valid email")
    private String[] emails;

    private String password;

    @NotEmpty(message = "user name is mandatory. please enter user name")
    private String userId;

    @NotEmpty(message = "mobile is mandatory. please enter mobile number ")
    private String mobile;

    private boolean status;
}
