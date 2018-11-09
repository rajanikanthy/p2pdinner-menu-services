package com.p2pdinner.services;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rajaniy on 1/25/17.
 */
@FeignClient(name = "P2PDINNER-PROFILE-SERVICES", serviceId = "P2PDINNER-PROFILE-SERVICES")
@Component
public interface ProfileServicesProxy {
    @RequestMapping(path = "oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    String requestToken(@RequestHeader("Authorization")String basicAuthorizationHeader,
                        @RequestBody String body);

    @RequestMapping(path = "/api/profiles/{id}", method = RequestMethod.GET)
    String validateProfile(@PathVariable("id") Integer id);


}
