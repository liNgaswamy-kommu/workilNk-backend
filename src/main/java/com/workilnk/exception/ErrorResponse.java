package com.workilnk.exception;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
