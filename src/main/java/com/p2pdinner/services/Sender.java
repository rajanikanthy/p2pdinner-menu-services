package com.p2pdinner.services;

import com.p2pdinner.domain.DinnerListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Created by rajaniy on 2/7/17.
 */
@EnableBinding(DinnerListingSource.class)
public class Sender {
    @Output(DinnerListingSource.DINNER_LISTING_Q)
    @Autowired
    private MessageChannel messageChannel;

    public void send(DinnerListing dinnerListing) {
        messageChannel.send(MessageBuilder.withPayload(dinnerListing).build());
    }
}
