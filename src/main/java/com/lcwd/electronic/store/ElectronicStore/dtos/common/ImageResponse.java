package com.lcwd.electronic.store.ElectronicStore.dtos.common;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponse {
    private String name;
    private String message;
    private boolean success;
    private HttpStatus status;
}
