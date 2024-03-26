package com.networkKnights.ecms.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.Date;

@Getter
@Setter
public class DepartmentDto {
    private long departmentId;
    private String departmentName;
    private String departmentCode;
    private String skills;
    private String headedBy;
    private String headedByName;
    private String costing;
    private boolean isActive;
}
