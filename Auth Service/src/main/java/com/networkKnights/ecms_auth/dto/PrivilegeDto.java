package com.networkKnights.ecms_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDto {
    long priviledgeId;
    String priviledge;
    String priviledgeName;
}
