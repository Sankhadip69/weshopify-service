package com.springframework.platform.bean;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CategoryBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -8236057778771058668L;

    private int id;
    private String name;
    private String alias;
    private int pcategory;
    private boolean enabled;
}
