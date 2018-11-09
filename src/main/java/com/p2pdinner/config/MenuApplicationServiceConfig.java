package com.p2pdinner.config;

import com.p2pdinner.filters.ProfileValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by rajaniy on 11/5/16.
 */
@Configuration
public class MenuApplicationServiceConfig {

    @Autowired
    private ProfileValidatorFilter profileValidatorFilter;

    @Bean
    public FilterRegistrationBean profileFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(profileValidatorFilter);
        registrationBean.setName("Profile Filter Bean");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean(name = "profileValidatorClient")
    public RestTemplate profileValidatorClient() {
        return new RestTemplate();
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
