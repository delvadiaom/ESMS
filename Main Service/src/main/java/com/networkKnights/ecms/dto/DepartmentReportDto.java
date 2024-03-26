package com.networkKnights.ecms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class DepartmentReportDto {
    private String departmentName;
    private String departmentCode;
    private String headedBy;
    private String headedByName;
    private String costing;
    private boolean isActive;
}
