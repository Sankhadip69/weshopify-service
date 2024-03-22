package com.springframework.platform.cqrs.handler;

import com.springframework.platform.cqrs.commands.CategoryCommand;
import com.springframework.platform.cqrs.events.CategoryEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Slf4j
public class CategoriesAggregate {

    @AggregateIdentifier
    private String eventId;
    private int id;
    private String name;
    private String alias;
    private int pcategory;
    private boolean enabled;

    @CommandHandler
    public CategoriesAggregate(CategoryCommand command) {
        log.info("step-2: command handler recived the command and creating an event");
        CategoryEvent event = CategoryEvent.builder()
                .id(command.getId())
                .eventId(command.getEventId())
                .name(command.getName())
                .alias(command.getAlias())
                .pcategory(command.getPcategory())
                .enabled(command.isEnabled())
                .build();

        log.info("step-3: Publishing the created event to the Event Handler");
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(CategoryEvent event) {

        log.info("step-4:Event Handler recived the event");
        this.eventId = event.getEventId();
        this.id = event.getId();
        this.name = event.getName();
        this.alias = event.getAlias();
        this.pcategory = event.getPcategory();
        this.enabled = event.isEnabled();
    }
}
