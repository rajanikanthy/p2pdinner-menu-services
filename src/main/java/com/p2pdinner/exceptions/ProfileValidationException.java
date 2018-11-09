package com.p2pdinner.exceptions;

/**
 * Created by rajaniy on 11/16/16.
 */
public class ProfileValidationException extends RuntimeException {
    public ProfileValidationException(String message) {
        super(message);
    }
}
