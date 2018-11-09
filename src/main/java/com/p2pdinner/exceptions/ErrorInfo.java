package com.p2pdinner.exceptions;

import java.io.Serializable;

/**
 * Created by rajaniy on 11/17/16.
 */
public class ErrorInfo implements Serializable {
    private String exception;
    private String error;
    private String uri;
    private long timestamp;
    private int status;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
