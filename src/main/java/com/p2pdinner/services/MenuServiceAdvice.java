package com.p2pdinner.services;

import com.p2pdinner.domain.MenuItem;
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

    public MenuServiceAdvice(RedisTemplate redisTemplate, RabbitTemplate rabbitTemplate) {
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Before("execution (* com.p2pdinner.services.MenuService.createMenuItem(com.p2pdinner.domain.MenuItem)) && args(menuItem)")
    public void createMenuItemAdvice(MenuItem menuItem) {
        _logger.info("Writing to Cache ... {}", menuItem.getTitle());
        _logger.info("Sending to Queue ... {}", menuItem.getTitle());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        _logger.info(">>>> Aspect initialized <<<<<");
    }
}
