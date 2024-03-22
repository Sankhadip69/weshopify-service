package com.springframework.platform.cqrs.commands;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCommand implements Serializable {

    @Serial
    private static final long serialVersionUID = 8525170254794422583L;

    @AggregateIdentifier
    private String eventId;
    private int id;
    private String name;
    private String alias;
    private int pcategory;
    private boolean enabled;

}
