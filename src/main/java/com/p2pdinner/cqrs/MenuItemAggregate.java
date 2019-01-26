package com.p2pdinner.cqrs;


import com.p2pdinner.commands.CreateMenuItemCommand;
import com.p2pdinner.events.CreateMenuItemEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class MenuItemAggregate {

    private static final Logger _logger = LoggerFactory.getLogger(MenuItemAggregate.class);

    @AggregateIdentifier
    private Integer menuItemId;

    public MenuItemAggregate() {

    }

    @CommandHandler
    public void createMenuItemHandler(CreateMenuItemCommand createMenuItemCommand) {
        _logger.info("Invoked createMenuItemHandler with menuItemId... {}", createMenuItemCommand.getMenuItemId());
        apply(new CreateMenuItemEvent(createMenuItemCommand.getMenuItem()));
    }

    @EventSourcingHandler
    public void on(CreateMenuItemCommand cme) {
        _logger.info("EventSourcingHandler..... CreateMenuItemCommand");
    }


}
