package com.MooBoo.MooBoo_Spring.domain.bookapi.exception;

public class BookApiBadRequestException extends RuntimeException {
    public BookApiBadRequestException(String message) {
        super(message);
    }
}
