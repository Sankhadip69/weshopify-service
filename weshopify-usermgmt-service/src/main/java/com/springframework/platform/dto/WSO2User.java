package com.springframework.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springframework.platform.bean.RoleBean;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class WSO2User implements Serializable {

    @Serial
    private static final long serialVersionUID = 2683538313486353058L;

    private String[] schemas;

    private String userName;

    private String password;

    private String[] emails;

    @JsonIgnore
    private String id;

    private WSO2UserPersonals name;

    @JsonIgnore
    private List<RoleBean> roleBeanList;

    private List<WSO2PhoneNumbers> phoneNumbers;
}
