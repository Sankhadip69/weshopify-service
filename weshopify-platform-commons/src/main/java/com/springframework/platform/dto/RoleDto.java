package com.springframework.platform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
@Builder
public class RoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2126965853397913711L;

    private String type;
    private String value;
    private String display;

    @JsonProperty("$ref")
    private String ref;

}
