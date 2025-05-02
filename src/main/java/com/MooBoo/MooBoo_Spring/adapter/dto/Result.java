package com.MooBoo.MooBoo_Spring.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class Result<T> {
    private String message;
    private HttpStatus status;
    private T data;

    public static <T> Result<T> wrapper(String message, HttpStatus status,T data) {
        Result<T> result = new Result<>(message, status, data);
        return result;
    }
}
