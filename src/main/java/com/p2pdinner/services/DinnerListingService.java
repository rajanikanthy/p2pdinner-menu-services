package com.p2pdinner.services;

import com.p2pdinner.domain.DinnerCategory;
import com.p2pdinner.domain.DinnerDelivery;
import com.p2pdinner.domain.DinnerSpecialNeeds;
import com.p2pdinner.domain.MenuItem;
import com.p2pdinner.proto.DinnerListingsServiceGrpc;
import com.p2pdinner.proto.ListingResponse;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Component
public class DinnerListingService {
    private static final Logger _logger = LoggerFactory.getLogger(DinnerListingService.class);

    private final ManagedChannel channel;

    public DinnerListingService(ManagedChannel channel) {
        this.channel = channel;
    }

    public class MenuItemTransformation implements Function<MenuItem, com.p2pdinner.proto.MenuItem> {

        @Override
        public com.p2pdinner.proto.MenuItem apply(MenuItem menuItem) {

            List<com.p2pdinner.proto.MenuItem.DinnerSpecialNeeds> specialNeedsList = new ArrayList<>();
            List<com.p2pdinner.proto.MenuItem.DinnerCategory> categoryList = new ArrayList<>();
            List<com.p2pdinner.proto.MenuItem.DinnerDelivery> dinnerDeliveryList = new ArrayList<>();

            if (!menuItem.getSpecialNeeds().isEmpty()) {
                for(Iterator<DinnerSpecialNeeds> iter = menuItem.getSpecialNeeds().iterator();  iter.hasNext(); ) {
                    DinnerSpecialNeeds specialNeeds = iter.next();
                    specialNeedsList.add(com.p2pdinner.proto.MenuItem.DinnerSpecialNeeds.newBuilder()
                            .setName(specialNeeds.getName())
                            .setId(specialNeeds.getId())
                            .build());
                }
            }

            if (!menuItem.getCategories().isEmpty()) {
                for(Iterator<DinnerCategory> iter = menuItem.getCategories().iterator(); iter.hasNext(); ) {
                    DinnerCategory dinnerCategory = iter.next();
                    categoryList.add(com.p2pdinner.proto.MenuItem.DinnerCategory.newBuilder()
                            .setName(dinnerCategory.getName())
                            .setId(dinnerCategory.getId())
                            .build());
                }
            }

            if (!menuItem.getDeliveries().isEmpty()) {
                for(Iterator<DinnerDelivery> iter = menuItem.getDeliveries().iterator(); iter.hasNext(); ) {
                    DinnerDelivery dinnerDelivery = iter.next();
                    dinnerDeliveryList.add(com.p2pdinner.proto.MenuItem.DinnerDelivery.newBuilder()
                            .setName(dinnerDelivery.getName())
                            .setId(dinnerDelivery.getId())
                            .build());
                }
            }

            return com.p2pdinner.proto.MenuItem.newBuilder()
                    .setTitle(menuItem.getTitle())
                    .setProfileId(menuItem.getProfileId())
                    .setId(menuItem.getId())
                    .setAddressLine1(menuItem.getAddressLine1())
                    .setAvailableQuantity(menuItem.getAvailableQuantity())
                    .setStartDate(menuItem.getStartDate())
                    .setCloseDate(menuItem.getCloseDate())
                    .setEndDate(menuItem.getEndDate())
                    .setCostPerItem(menuItem.getCostPerItem())
                    .setState(menuItem.getState())
                    .addAllCategories(categoryList)
                    .addAllDeliveries(dinnerDeliveryList)
                    .addAllSpecialNeeds(specialNeedsList)
                    .build();
        }
    }

    public ListingResponse addToListing(MenuItem menuItem) {
        com.p2pdinner.proto.MenuItem mi = new MenuItemTransformation().apply(menuItem);
        DinnerListingsServiceGrpc.DinnerListingsServiceBlockingStub service = DinnerListingsServiceGrpc.newBlockingStub(channel);
        return service.addDinnerListing(mi);
    }

    public ListingResponse updateListing(MenuItem menuItem) {
        com.p2pdinner.proto.MenuItem mi = new MenuItemTransformation().apply(menuItem);
        DinnerListingsServiceGrpc.DinnerListingsServiceBlockingStub service = DinnerListingsServiceGrpc.newBlockingStub(channel);
        ListingResponse listingResponse = service.updateDinnerListing(mi);
        if (!listingResponse.getSuccess()) {
            _logger.error("Error updating dinner listing");
        }
        return listingResponse;
    }

    public ListingResponse deleteListing(MenuItem menuItem) {
        com.p2pdinner.proto.MenuItem mi = new MenuItemTransformation().apply(menuItem);
        DinnerListingsServiceGrpc.DinnerListingsServiceBlockingStub service = DinnerListingsServiceGrpc.newBlockingStub(channel);
        ListingResponse listingResponse = service.deleteDinnerListing(mi);
        return listingResponse;
    }
}
