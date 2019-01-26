package com.p2pdinner.events;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MenuItemEventProcessor {
    @EventHandler
    public void on(CreateMenuItemEvent cme) {
        log.info("Create Menu Item received with Id ... {}", cme.getMenuItem().getId());
    }
}
