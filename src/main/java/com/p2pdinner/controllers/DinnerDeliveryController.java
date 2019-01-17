package com.p2pdinner.controllers;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.mappers.DinnerDeliveryMapper;
import com.p2pdinner.mappers.DinnerSpecialNeedsMapper;
import com.p2pdinner.mappers.MenuItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rajaniy on 1/27/17.
 */
@RestController
public class DinnerDeliveryController {
    private Logger _logger = LoggerFactory.getLogger(DinnerDeliveryController.class);

    @Autowired
    private DinnerDeliveryMapper dinnerDeliveryMapper;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/deliveryTypes")
    public ResponseEntity<?> allCategories() {
        return ResponseEntity.ok(dinnerDeliveryMapper.findAllDeliveryTypes());
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/{profileId}/menuitem/{menuItemId}/deliveryType")
    public ResponseEntity<?> categories(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") Integer menuItemId) {
        return ResponseEntity.ok(dinnerDeliveryMapper.getDeliveryByMenuId(profileId, menuItemId));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/{profileId}/menuitem/{menuItemId}/deliveryType")
    public ResponseEntity<?> addSpecialNeed(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") Integer menuItemId, @RequestBody DinnerDelivery dinnerDelivery) {
        DinnerDelivery dc = dinnerDeliveryMapper.deliveryByName(dinnerDelivery.getName());
        MenuItem menuItem = menuItemMapper.findMenuItemById(profileId, menuItemId);
        if (dc != null) {
            dinnerDeliveryMapper.associateDeliveryTypeWithMenuItem(menuItem.getId(), dc.getId());
            return ResponseEntity.ok(dinnerDelivery);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "/api/{profileId}/menuitem/{menuItemId}/categories/{deliveryTypeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategory(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") Integer menuItemId, @PathVariable("deliveryTypeId") Integer dinnerCategoryId) {
        dinnerDeliveryMapper.disassociateDeliveryTypeWithMenuItem(menuItemId, dinnerCategoryId);
        return ResponseEntity.ok().build();
    }
}
