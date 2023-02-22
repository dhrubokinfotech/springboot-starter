package com.disl.boilerplate.exceptions;

import org.springframework.http.HttpStatus;

public class ResponseException extends RuntimeException {

    private Object payload;
    private HttpStatus httpStatus;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ResponseException(HttpStatus httpStatus, String message, Object payload) {
        super(message);
        this.payload = payload;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
