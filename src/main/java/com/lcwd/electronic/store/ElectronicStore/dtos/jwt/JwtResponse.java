package com.lcwd.electronic.store.ElectronicStore.dtos.jwt;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String jwtToken;
    private UserDto user;
    private String refreshToken;
}
