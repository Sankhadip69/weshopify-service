package com.springframework.platform.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class Auditor implements Serializable {


    @Serial
    private static final long serialVersionUID = 4513814360854556246L;

    private Date createdDate;

    private Date modifiedDate;

    private String createdBy;
    private String modifiedBy;
}
