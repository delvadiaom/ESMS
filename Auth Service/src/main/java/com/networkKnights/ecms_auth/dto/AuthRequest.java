package com.networkKnights.ecms_auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRequest {
    String username;
    String password;
}
