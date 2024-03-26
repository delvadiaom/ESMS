package com.networkKnights.ecms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TeamGraphDto {
    private String teamName;
    private String costing;
    private boolean isActive;
}
