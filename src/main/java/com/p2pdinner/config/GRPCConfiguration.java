package com.p2pdinner.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GRPCConfiguration {

    @Bean
    public ManagedChannel channel() {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("listing-service", 6565)
                .usePlaintext()
                .build();
        log.info("Channel configured with parameters {}", channel);
        return channel;
    }
}
