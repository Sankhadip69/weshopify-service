package com.springframework.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Jacksonized
@Data
@Builder
public class WSO2User implements Serializable {

    @Serial
    private static final long serialVersionUID = 2683538313486353058L;

    private String[] schemas;

    private String userName;

    private String password;

    private String[] emails;

    //@JsonIgnore
    private String id;

    private WSO2UserPersonals name;

    //@JsonIgnore
    private List<RoleDto> roleBeanList;

    private List<WSO2PhoneNumbers> phoneNumbers;
}
