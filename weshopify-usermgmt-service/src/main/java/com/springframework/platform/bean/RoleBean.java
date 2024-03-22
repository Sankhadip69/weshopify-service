package com.springframework.platform.bean;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleBean implements Serializable {
    @Serial
    private static final long serialVersionUID = -2126965853397913711L;

    private String id;
    private String displayName;
    private List<String> permissions;
    private String[] schemas;
}
