package com.p2pdinner.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GRPCConfiguration {

    @Value("${app.dinner.listing.grpc.url}")
    private String grpcServerUrl;

    @Value("${app.dinner.listing.grpc.port}")
    private Integer port;

    @Bean
    public ManagedChannel channel() {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcServerUrl, port)
                .usePlaintext()
                .build();
        log.info("Channel configured with parameters {}", channel);
        return channel;
    }
}
