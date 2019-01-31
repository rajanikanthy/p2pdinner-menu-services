package com.p2pdinner.controllers;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.mappers.DinnerCategoryMapper;
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
public class DinnerSpeicalNeedsController {
    private Logger _logger = LoggerFactory.getLogger(DinnerSpeicalNeedsController.class);

    @Autowired
    private DinnerSpecialNeedsMapper dinnerSpecialNeedsMapper;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/specialNeeds")
    public ResponseEntity<?> allCategories() {
        return ResponseEntity.ok(dinnerSpecialNeedsMapper.findAllSpecialNeeds());
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/{profileId}/menuitem/{menuItemId}/specialNeeds")
    public ResponseEntity<?> categories(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") String menuItemId) {
        return ResponseEntity.ok(dinnerSpecialNeedsMapper.getSpecialNeedsByMenuId(profileId, menuItemId));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/{profileId}/menuitem/{menuItemId}/specialNeeds")
    public ResponseEntity<?> addSpecialNeed(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") String menuItemId, @RequestBody DinnerSpecialNeeds dinnerSpecialNeeds) {
        DinnerSpecialNeeds dc = dinnerSpecialNeedsMapper.specialNeedsByName(dinnerSpecialNeeds.getName());
        MenuItem menuItem = menuItemMapper.findMenuItemById(profileId, menuItemId);
        if (dc != null) {
            dinnerSpecialNeedsMapper.associateSpecialNeedsWithMenuItem(menuItem.getId(), dc.getId());
            return ResponseEntity.ok(dinnerSpecialNeeds);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "/api/{profileId}/menuitem/{menuItemId}/categories/{specialNeedsId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategory(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") String menuItemId, @PathVariable("dinnerCategoryId") Integer dinnerCategoryId) {
        dinnerSpecialNeedsMapper.disassociateSpecialNeedsWithMenuItem(menuItemId, dinnerCategoryId);
        return ResponseEntity.ok().build();
    }
}
