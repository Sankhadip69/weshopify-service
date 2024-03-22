package com.springframework.platform.entity;

import com.springframework.platform.dto.Auditor;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "categories")
@Data

public class Category extends Auditor implements Serializable {

    @Serial
    private static final long serialVersionUID = 8525170254794422583L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true,nullable = false,updatable = true)
    private String name;

    @Column(unique = false,updatable = true)
    private String alias;

    private String imagePath;

    @Column(nullable = false,updatable = true)
    private boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    private Category parent;

}
