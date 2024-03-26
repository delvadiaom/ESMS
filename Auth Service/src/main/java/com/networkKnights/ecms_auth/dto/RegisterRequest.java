package com.networkKnights.ecms_auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class RegisterRequest {
    private String password;
    private String empCode;
    private String email;
    private long priviledgeId;
}
