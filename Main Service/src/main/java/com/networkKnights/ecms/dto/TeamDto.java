package com.networkKnights.ecms.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class TeamDto {

    private long teamId;
    private String teamCode;
    private String teamName;
    private String leadBy;
    private String leadByName;
    private String costing;
    private long departmentId;
    private boolean isActive;
    private int employeeCount;
}
