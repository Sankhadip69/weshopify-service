package com.springframework.platform.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class WSO2UserAuthBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -788198082554206756L;

    private String grant_type;

    private String username;

    private String password;

    private String scope;

}
