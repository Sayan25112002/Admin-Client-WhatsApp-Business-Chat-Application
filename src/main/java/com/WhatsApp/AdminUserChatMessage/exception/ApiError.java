package com.WhatsApp.AdminUserChatMessage.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private String message;
    private HttpStatus status;
    private LocalDateTime timestamp;

    public ApiError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}
