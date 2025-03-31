package com.MooBoo.MooBoo_Spring.domain.exception;

public class BookApiBadRequestException extends RuntimeException {
    public BookApiBadRequestException(String message) {
        super(message);
    }
}
