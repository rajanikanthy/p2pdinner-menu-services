package com.p2pdinner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.p2pdinner.cqrs.EventType;
import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.mappers.DinnerCategoryMapper;
import com.p2pdinner.mappers.DinnerDeliveryMapper;
import com.p2pdinner.mappers.DinnerSpecialNeedsMapper;
import com.p2pdinner.mappers.MenuItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Component
public class MenuService {
    private static final Logger _logger = LoggerFactory.getLogger(MenuService.class);

    private final MenuItemMapper menuItemMapper;
    private final DinnerCategoryMapper dinnerCategoryMapper;
    private final DinnerSpecialNeedsMapper dinnerSpecialNeedsMapper;
    private final DinnerDeliveryMapper dinnerDeliveryMapper;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MenuService(MenuItemMapper menuItemMapper,
                       DinnerCategoryMapper dinnerCategoryMapper,
                       DinnerSpecialNeedsMapper dinnerSpecialNeedsMapper,
                       DinnerDeliveryMapper dinnerDeliveryMapper,
                       RabbitTemplate rabbitTemplate) {
        this.menuItemMapper = menuItemMapper;
        this.dinnerCategoryMapper = dinnerCategoryMapper;
        this.dinnerSpecialNeedsMapper = dinnerSpecialNeedsMapper;
        this.dinnerDeliveryMapper = dinnerDeliveryMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Collection<MenuItem> menuItemsByProfile(Integer profileId) {
        return menuItemMapper.findAllMenuItemsById(profileId);
    }

    public MenuItem menuItemById(Integer profileId, Integer menuItemId) {
        MenuItem menuItem = menuItemMapper.findMenuItemById(profileId, menuItemId);
        Collection<DinnerCategory> dinnerCategories = dinnerCategoryMapper.getCategoriesByMenuId(profileId, menuItemId);
        Collection<DinnerSpecialNeeds> dinnerSpecialNeeds = dinnerSpecialNeedsMapper.getSpecialNeedsByMenuId(profileId, menuItemId);
        Collection<DinnerDelivery> dinnerDeliveries = dinnerDeliveryMapper.getDeliveryByMenuId(profileId, menuItemId);
        menuItem.setCategories(dinnerCategories);
        menuItem.setSpecialNeeds(dinnerSpecialNeeds);
        menuItem.setDeliveries(dinnerDeliveries);
        return menuItem;
    }

    @Transactional
    public MenuItem createMenuItem(MenuItem menuItem) {
        rabbitTemplate.convertAndSend("menu-exchange", EventType.CMD_CREATE.name(), menuItem);
        menuItemMapper.createMenuItem(menuItem);
        if (menuItem.getCategories() != null && !menuItem.getCategories().isEmpty()) {
            menuItem.getCategories().forEach( category -> {
                dinnerCategoryMapper.associateCategoryWithMenuItem(menuItem.getId(), category.getId());
            });
        }
        if (menuItem.getSpecialNeeds() != null && !menuItem.getSpecialNeeds().isEmpty()) {
            menuItem.getSpecialNeeds().forEach( category -> {
                dinnerSpecialNeedsMapper.associateSpecialNeedsWithMenuItem(menuItem.getId(), category.getId());
            });
        }
        if (menuItem.getDeliveries() != null && !menuItem.getDeliveries().isEmpty()) {
            menuItem.getDeliveries().forEach( category -> {
                dinnerDeliveryMapper.associateDeliveryTypeWithMenuItem(menuItem.getId(), category.getId());
            });
        }
        return menuItemById(menuItem.getProfileId(), menuItem.getId());
    }

    @Transactional
    public void deleteMenuItem(MenuItem menuItem) {
        if (!CollectionUtils.isEmpty(menuItem.getCategories())) {
            menuItem.getCategories().forEach( category -> dinnerCategoryMapper.disassociateCategoryWithMenuItem(menuItem.getId(), category.getId()));
        }
        if (!CollectionUtils.isEmpty(menuItem.getSpecialNeeds())) {
            menuItem.getSpecialNeeds().forEach( specialNeeds -> dinnerSpecialNeedsMapper.disassociateSpecialNeedsWithMenuItem(menuItem.getId(), specialNeeds.getId()));
        }
        if (!CollectionUtils.isEmpty(menuItem.getDeliveries())) {
            menuItem.getDeliveries().forEach(dinnerDelivery -> dinnerDeliveryMapper.disassociateDeliveryTypeWithMenuItem(menuItem.getId(), dinnerDelivery.getId()));
        }
        menuItemMapper.deleteMenuItem(menuItem.getProfileId(), menuItem.getId());
    }
}
