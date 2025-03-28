package com.lcwd.electronic.store.ElectronicStore.dtos.jwt;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest {
    private String username;
    private String password;
}
