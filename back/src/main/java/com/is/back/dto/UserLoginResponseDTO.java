package com.is.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String username;
    private Long userId;
    private String token;
    private String role;
}
