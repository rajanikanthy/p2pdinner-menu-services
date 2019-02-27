package com.p2pdinner.services;

import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.proto.ListingResponse;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MenuServiceAdvice implements InitializingBean {

    private static final Logger _logger = LoggerFactory.getLogger(MenuServiceAdvice.class);

    private final RedisTemplate redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final DinnerListingService dinnerListingService;

    public MenuServiceAdvice(RedisTemplate redisTemplate,
                             RabbitTemplate rabbitTemplate,
                             DinnerListingService dinnerListingService) {
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.dinnerListingService = dinnerListingService;
    }

    @Before("execution (* com.p2pdinner.services.MenuService.createMenuItem(com.p2pdinner.domain.MenuItem)) && args(menuItem)")
    public void createMenuItemAdvice(MenuItem menuItem) {
        _logger.info("Writing to Cache ... {}", menuItem.getTitle());
        ListingResponse response = dinnerListingService.addToListing(menuItem);
        if (response.getSuccess()) {
            _logger.info("Listing added successfully");
        } else {
            _logger.error("Error adding listing");
        }
    }

    //updateMenuItem
    @After("execution (* com.p2pdinner.services.MenuService.updateMenuItem(com.p2pdinner.domain.MenuItem)) && args(menuItem)")
    public void updateMenuItemAdvice(MenuItem menuItem) {
        ListingResponse response = dinnerListingService.updateListing(menuItem);
        if (response.getSuccess()) {
            _logger.info("Listing updated successfully");
        } else {
            _logger.error("Error updating listing");
        }
    }

    @After("execution (* com.p2pdinner.services.MenuService.deleteMenuItem(com.p2pdinner.domain.MenuItem)) && args(menuItem)")
    public void deleteMenuItemAdvice(MenuItem menuItem) {
        ListingResponse response = dinnerListingService.deleteListing(menuItem);
        if (response.getSuccess()) {
            _logger.info("Listing deleted successfully");
        } else {
            _logger.error("Error deleting listing");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        _logger.info(">>>> Aspect initialized <<<<<");
    }
}
