package com.p2pdinner.controllers;

import com.p2pdinner.MenuService;
import com.p2pdinner.domain.*;
import com.p2pdinner.domain.vo.MenuItemVO;
import com.p2pdinner.filepicker.FilePickerOperations;
import com.p2pdinner.filepicker.FilePickerUploadResponse;
import com.p2pdinner.mappers.DinnerCategoryMapper;
import com.p2pdinner.mappers.DinnerDeliveryMapper;
import com.p2pdinner.mappers.DinnerSpecialNeedsMapper;
import com.p2pdinner.mappers.MenuItemMapper;
import com.p2pdinner.services.MenuItemTransformationService;
import com.p2pdinner.services.ProfileServicesProxy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private MenuItemMapper menuItemMapper;

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
    public ResponseEntity<MenuItem> itemById(@PathVariable("profileId") Integer profileId, @PathVariable("id")Integer id) {
        return ResponseEntity.ok(menuService.menuItemById(profileId, id));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMenuItem(@PathVariable("profileId") Integer profileId , @RequestBody MenuItemVO mi) {
        MenuItem menuItem = menuItemTransformationService.apply(mi);
        menuItem.setProfileId(profileId);
        MenuItem createdItem = menuService.createMenuItem(menuItem);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdItem.getId()).toUri()).build();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMenuItem(@PathVariable("profileId") Integer profileId, @PathVariable Integer id, @RequestBody MenuItem menuItem) {
        MenuItem foundMenuItem = menuItemMapper.findMenuItemById(profileId, id);
        if (foundMenuItem == null) {
            return ResponseEntity.notFound().build();
        }
        menuItem.setId(id);
        menuItemMapper.updateMenuItem(menuItem);
        return ResponseEntity.ok(menuItem);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity patchMenuItem(@PathVariable("profileId")Integer profileId, @PathVariable Integer id, @RequestBody MenuItem menuItem) {
        MenuItem foundMenuItem = menuItemMapper.findMenuItemById(profileId, id);
        if (foundMenuItem == null) {
            return ResponseEntity.notFound().build();
        }
        menuItem.setId(id);
        menuItem.setProfileId(profileId);
        menuItemMapper.partialUpdateMenuItem(menuItem);
        MenuItem updatedItem = menuItemMapper.findMenuItemById(profileId, id);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteMenuItem(@PathVariable("profileId")Integer profileId, @PathVariable Integer id) {
        MenuItem foundMenuItem = menuService.menuItemById(profileId, id);
        if (foundMenuItem == null) {
            return ResponseEntity.notFound().build();
        }
        menuService.deleteMenuItem(foundMenuItem);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{id}/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleImageUpload(@PathVariable("profileId")Integer profileId, @PathVariable("id") Integer menuItemId, @RequestParam("file")MultipartFile image) {
        String uri = "";
        try {
            if (StringUtils.hasText(image.getOriginalFilename())) {
                _logger.info(image.getOriginalFilename());
                if (isValidImageExtn(image.getOriginalFilename())) {
                    String fileExtn = fileExtn(image.getOriginalFilename());
                    java.nio.file.Path path = Files.createTempFile("img" + System.currentTimeMillis(), "." + fileExtn);
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    _logger.info("Temp File {}", path);
                    // Loading into s3
                    FilePickerUploadResponse response = filePickerOperations.upload(path.toString());
                    uri = response.getUrl();
                    MenuItem menuItem = menuItemMapper.findMenuItemById(profileId, menuItemId);
                    if (menuItem != null) {
                        if (menuItem.getImageUri() != null && StringUtils.hasText(menuItem.getImageUri())) {
                            menuItem.setImageUri(menuItem.getImageUri() + "," + uri);
                        } else {
                            menuItem.setImageUri(uri);
                        }
                        menuItemMapper.updateMenuItem(menuItem);
                    }
                    return ResponseEntity.ok(menuItem);
                }
            }
        } catch (Exception e) {
            _logger.error(e.getMessage(), e);
            //throw excepBuilder.createException(ErrorCode.UNKNOWN, new Object[]{ e.getMessage() }, Locale.US);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list(@PathVariable("profileId") Integer profileId, @PathVariable("id") Integer menuItemId, @RequestBody DinnerListing dinnerListing) {
        MenuItem menuItem = menuItemMapper.findMenuItemById(profileId, menuItemId);
        if (menuItem != null) {
            if (menuItem.getAddressLine1() == null || !StringUtils.hasText(menuItem.getAddressLine1())) {
                return ResponseEntity.badRequest().body("Invalid address. Please update menu item with address before listing.");
            }
            if (menuItem.getStartDate() > menuItem.getCloseDate() || menuItem.getStartDate() > menuItem.getEndDate()) {
                return ResponseEntity.badRequest().body("Invalid startDate. Please update menu item with start date before close/end date.");
            }
            if (menuItem.getEndDate() < menuItem.getCloseDate()) {
                return ResponseEntity.badRequest().body("Invalid endDate. Please update menu item with end date after close date.");
            }
            dinnerListing.setAddressLine1(menuItem.getAddressLine1());
            dinnerListing.setAddressLine2(menuItem.getAddressLine2());
            dinnerListing.setCity(menuItem.getCity());
            dinnerListing.setCategories(StringUtils.collectionToCommaDelimitedString(menuItem.getCategories()));
            dinnerListing.setCloseTime(menuItem.getCloseDate());
            dinnerListing.setStartTime(menuItem.getStartDate());
            dinnerListing.setEndTime(menuItem.getEndDate());
            dinnerListing.setTitle(menuItem.getTitle());
            dinnerListing.setSellerProfileId(profileId);
            dinnerListing.setDeliveryTypes(StringUtils.collectionToCommaDelimitedString(menuItem.getDeliveries()));
            dinnerListing.setSpecialNeeds(StringUtils.collectionToCommaDelimitedString(menuItem.getSpecialNeeds()));
            return ResponseEntity.ok(dinnerListing);
        }
        return ResponseEntity.badRequest().build();
    }


    private  static Boolean isValidImageExtn(String filename) {
        Boolean validExtn = Boolean.FALSE;
        for(String imageExtn : IMAGE_EXTNS) {
            if (!validExtn && filename.toLowerCase().endsWith(imageExtn)) {
                validExtn = Boolean.TRUE;
            }
        }
        return validExtn;
    }

    private static String fileExtn(String filename) {
        if (org.apache.commons.lang.StringUtils.isNotEmpty(filename) && filename.contains(".")) {
            String[] fileParts = filename.split("\\.");
            return fileParts[fileParts.length - 1];
        }
        return org.apache.commons.lang.StringUtils.EMPTY;
    }

}
