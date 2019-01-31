package com.p2pdinner.controllers;

import com.p2pdinner.services.MenuService;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.domain.vo.MenuItemVO;
import com.p2pdinner.exceptions.MenuItemNotFoundException;
import com.p2pdinner.filepicker.FilePickerOperations;
import com.p2pdinner.mappers.DinnerCategoryMapper;
import com.p2pdinner.mappers.DinnerDeliveryMapper;
import com.p2pdinner.mappers.DinnerSpecialNeedsMapper;
import com.p2pdinner.services.MenuItemTransformationService;
import com.p2pdinner.services.ProfileServicesProxy;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

/**
 * Created by rajaniy on 11/2/16.
 */
@RestController
@RequestMapping("/api/{profileId}/menuitem")
@Api(value="MenuItemController", description="Operations pertaining to Menu Items By profile", tags = "MenuItems")
public class MenuItemController {

    private static final Logger _logger = LoggerFactory.getLogger(MenuItemController.class);
    private static final String[] IMAGE_EXTNS = { ".png", ".jpg", ".gif", ".bmp", "jpeg" };

    private final MenuService menuService;

    @Autowired
    private DinnerCategoryMapper dinnerCategoryMapper;

    @Autowired
    private DinnerSpecialNeedsMapper dinnerSpecialNeedsMapper;

    @Autowired
    private DinnerDeliveryMapper dinnerDeliveryMapper;

    @Autowired
    private FilePickerOperations filePickerOperations;

    @Autowired
    private ProfileServicesProxy profileServicesProxy;

    @Autowired
    private MenuItemTransformationService menuItemTransformationService;

    @Autowired
    public MenuItemController(MenuService menuService) {
        this.menuService = menuService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<MenuItem>> itemsByProfile(@PathVariable("profileId") Integer profileId) {
        return ResponseEntity.ok(menuService.menuItemsByProfile(profileId));
    }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> itemById(@PathVariable("profileId") Integer profileId, @PathVariable("id")String id) {
        return ResponseEntity.ok(menuService.menuItemById(profileId, id));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMenuItem(@PathVariable("profileId") Integer profileId , @RequestBody MenuItemVO mi) {
        MenuItem menuItem = menuItemTransformationService.apply(mi);
        menuItem.setProfileId(profileId);
        try {
            MenuItem createdItem = menuService.createMenuItem(menuItem);
            return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdItem.getId()).toUri()).build();
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMenuItem(@PathVariable("profileId") Integer profileId, @PathVariable String id, @RequestBody MenuItem menuItem) {
        menuItem.setProfileId(profileId);
        menuItem.setId(id);
        try {
            menuService.updateMenuItem(menuItem);
            return ResponseEntity.ok(menuItem);
        } catch (MenuItemNotFoundException me) {
            _logger.error(me.getMessage(), me);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity patchMenuItem(@PathVariable("profileId")Integer profileId, @PathVariable String id, @RequestBody MenuItem menuItem) {
        menuItem.setProfileId(profileId);
        menuItem.setId(id);
        try {
            menuService.updateMenuItem(menuItem);
            return ResponseEntity.ok(menuItem);
        } catch (MenuItemNotFoundException me) {
            _logger.error(me.getMessage(), me);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMenuItem(@PathVariable("profileId")Integer profileId, @PathVariable String id) {
        MenuItem foundMenuItem = menuService.menuItemById(profileId, id);
        if (foundMenuItem == null) {
            return ResponseEntity.notFound().build();
        }
        menuService.deleteMenuItem(foundMenuItem);
        return ResponseEntity.ok().build();
    }

}
