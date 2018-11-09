package com.p2pdinner.controllers;

import com.p2pdinner.ProfileValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajaniy on 11/16/16.
 */
@RestControllerAdvice
public class MenuItemControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProfileValidationException.class)
    @ResponseBody
    Map<String,Object> handleProfileValidationException(HttpServletRequest httpServletRequest, ProfileValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new java.util.Date());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", ex.getMessage());
        response.put("exception", ex.getClass().getName());
        response.put("uri", httpServletRequest.getRequestURI());
        return response;
    }
}
