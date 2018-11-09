package com.p2pdinner.services;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by rajaniy on 2/7/17.
 */
public interface DinnerListingSource {
    public static String DINNER_LISTING_Q = "listingQ";

    @Output(DINNER_LISTING_Q)
    public MessageChannel listingQ();
}
