package com.dxvalley.epassbook.utils;

import lombok.Data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;

    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }


    public static <T> ResponseEntity<?> success(T response) {
        ApiResponse<T> apiResponse;
        if (response instanceof String)
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK, (String) response));
        else
            return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static <T> ResponseEntity<?> created(T response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public static <T> ResponseEntity<?> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, message));
    }
}

