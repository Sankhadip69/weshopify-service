package com.springframework.platform.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class BrandBean implements Serializable {

    @Serial
    private static final long serialVersionUID = -5167696846922942229L;

    @Id
    private int id;

    private String name;
    private String logoPath;
    //private List<String> categories;
    private Set<CategoryBean> categories;
}
