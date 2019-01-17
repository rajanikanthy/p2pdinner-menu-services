package com.p2pdinner.listeners;

import com.p2pdinner.domain.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private static final Logger _logger = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = "create-queue")
    public void processCreate(MenuItem menuItem) {
      _logger.info("Creating .... {}", menuItem.getTitle());
    }

    @RabbitListener(queues = "update-queue")
    public void processUpdate(String message) {
        _logger.info("Update.... {}", message);
    }

    @RabbitListener(queues = "delete-queue")
    public void processDelete(String message) {
        _logger.info("Delete....{}", message);
    }
}
