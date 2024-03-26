package com.networkKnights.ecms_auth.dto;

import lombok.Data;

import java.util.Date;


@Data
public class UserInfoDto {
    private long userId;
    private String empCode;
    private String email;
    private long priviledgeId;
    private String priviledgeName;
    private boolean isActive;
}
