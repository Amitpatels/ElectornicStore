package com.lcwd.electronic.store.ElectronicStore.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiResponseMessage {
    private String message;
    private boolean success;
    private HttpStatus status;
}
