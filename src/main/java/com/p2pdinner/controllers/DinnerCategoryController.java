package com.p2pdinner.controllers;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.mappers.DinnerCategoryMapper;
import com.p2pdinner.mappers.MenuItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by rajaniy on 1/27/17.
 */
@RestController
public class DinnerCategoryController {
    private Logger _logger = LoggerFactory.getLogger(DinnerCategoryController.class);

    @Autowired
    private DinnerCategoryMapper dinnerCategoryMapper;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/categories")
    public ResponseEntity<?> allCategories() {
        return ResponseEntity.ok(dinnerCategoryMapper.findAllCategories());
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/{profileId}/menuitem/{menuItemId}/categories")
    public ResponseEntity<?> categories(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") Integer menuItemId) {
        return ResponseEntity.ok(dinnerCategoryMapper.getCategoriesByMenuId(profileId, menuItemId));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, path = "/api/{profileId}/menuitem/{menuItemId}/categories")
    public ResponseEntity<?> addCategory(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") Integer menuItemId, @RequestBody DinnerCategory dinnerCategory) {
        DinnerCategory dc = dinnerCategoryMapper.categoryByName(dinnerCategory.getName());
        MenuItem menuItem = menuItemMapper.findMenuItemById(profileId, menuItemId);
        if (dc != null) {
            dinnerCategoryMapper.associateCategoryWithMenuItem(menuItem.getId(), dc.getId());
            return ResponseEntity.ok(dinnerCategory);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping(path = "/api/{profileId}/menuitem/{menuItemId}/categories/{dinnerCategoryId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCategory(@PathVariable("profileId") Integer profileId, @PathVariable("menuItemId") Integer menuItemId, @PathVariable("dinnerCategoryId") Integer dinnerCategoryId) {
        dinnerCategoryMapper.disassociateCategoryWithMenuItem(menuItemId, dinnerCategoryId);
        return ResponseEntity.ok().build();
    }
}
