package com.springframework.platform.entity;

import com.springframework.platform.bean.CategoryBean;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
public class Brand implements Serializable {

    @Serial
    private static final long serialVersionUID = -5167696846922942229L;

    @Id
    private int id;

    @Column(unique = true)
    private String name;
    private String logoPath;
    //private List<String> categories;
    private Set<CategoryBean> categories;
}
