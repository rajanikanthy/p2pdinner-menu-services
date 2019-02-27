package com.p2pdinner.services;

import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.vo.MenuItemVO;
import com.p2pdinner.mappers.DinnerCategoryMapper;
import com.p2pdinner.mappers.DinnerDeliveryMapper;
import com.p2pdinner.mappers.DinnerSpecialNeedsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MenuItemTransformationService implements Function<MenuItemVO, MenuItem> {

    private final DinnerCategoryMapper dinnerCategoryMapper;
    private final DinnerSpecialNeedsMapper dinnerSpecialNeedsMapper;
    private final DinnerDeliveryMapper dinnerDeliveryMapper;


    @Autowired
    public MenuItemTransformationService(DinnerCategoryMapper dinnerCategoryMapper,
                                         DinnerSpecialNeedsMapper dinnerSpecialNeedsMapper,
                                         DinnerDeliveryMapper dinnerDeliveryMapper) {
        this.dinnerDeliveryMapper = dinnerDeliveryMapper;
        this.dinnerSpecialNeedsMapper = dinnerSpecialNeedsMapper;
        this.dinnerCategoryMapper = dinnerCategoryMapper;
    }

    @Override
    public MenuItem apply(MenuItemVO menuItemVO) {
        MenuItem menuItem = new MenuItem();
        if (!StringUtils.hasText(menuItemVO.getId())) {
            menuItem.setId(UUID.randomUUID().toString());
        }
        menuItem.setTitle(menuItemVO.getTitle());
        menuItem.setDescription(menuItemVO.getDescription());
        menuItem.setIsActive(menuItemVO.getIsActive());
        menuItem.setAddressLine1(menuItemVO.getAddressLine1());
        menuItem.setAddressLine2(menuItemVO.getAddressLine2());
        menuItem.setZipCode(menuItemVO.getZipCode());
        menuItem.setState(menuItemVO.getState());
        menuItem.setCity(menuItemVO.getCity());
        menuItem.setAvailableQuantity(menuItemVO.getAvailableQuantity());
        menuItem.setCostPerItem(menuItemVO.getCostPerItem());
        menuItem.setImageUri(menuItemVO.getImageUri());
        menuItem.setState(menuItemVO.getState());
        menuItem.setStartDate(menuItemVO.getStartDate());
        menuItem.setEndDate(menuItemVO.getEndDate());
        menuItem.setCloseDate(menuItemVO.getCloseDate());
        if (menuItemVO.getCategories() != null && !menuItemVO.getCategories().isEmpty()) {
            menuItem.setCategories(this.dinnerCategoryMapper.findAllCategories()
                    .stream().filter(dinnerCategory -> {
                        return menuItemVO.getCategories().stream().anyMatch(c -> dinnerCategory.getName().equalsIgnoreCase(c));
                    }).collect(Collectors.toList()));
        }
        if (menuItemVO.getDeliveries() != null && !menuItemVO.getDeliveries().isEmpty()) {
            menuItem.setDeliveries(this.dinnerDeliveryMapper.findAllDeliveryTypes()
                    .stream().filter(deliveryType -> {
                        return menuItemVO.getDeliveries().stream().anyMatch(c -> deliveryType.getName().equalsIgnoreCase(c));
                    }).collect(Collectors.toList()));
        }
        if (menuItemVO.getSpecialNeeds() != null && !menuItemVO.getSpecialNeeds().isEmpty()) {
            menuItem.setSpecialNeeds(this.dinnerSpecialNeedsMapper.findAllSpecialNeeds()
                    .stream().filter(specialNeed -> {
                        return menuItemVO.getSpecialNeeds().stream().anyMatch(c -> specialNeed.getName().equalsIgnoreCase(c));
                    }).collect(Collectors.toList()));
        }

        return menuItem;
    }
}
