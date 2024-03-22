package com.springframework.platform.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.File;
import java.io.Serializable;

/**
 * @author : Sankhadip
 * @since : 26/12/2023
 * @apiNote : WeshopifyPlatformUser
 * {@summary }: WeshopifyPlatformUserBean is used to hold the user's data
 * and will be saved these users in WSO2 IAM
 */
@Data
@Builder
public class WeshopifyPlatformUserBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -1134906465402654798L;

    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private boolean status;
    private File photos;
}
