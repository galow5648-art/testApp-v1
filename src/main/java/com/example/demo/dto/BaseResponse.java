package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse {
    private boolean success;
    private String message;

    public static BaseResponse ok(String message) {
        return new BaseResponse(true, message);
    }

    public static BaseResponse error(String message) {
        return new BaseResponse(false, message);
    }
}