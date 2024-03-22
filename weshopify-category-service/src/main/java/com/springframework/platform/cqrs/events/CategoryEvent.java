package com.springframework.platform.cqrs.events;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
public class CategoryEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 8525170254794422583L;


    private String eventId;
    private int id;
    private String name;
    private String alias;
    private int pcategory;
    private boolean enabled;

}
