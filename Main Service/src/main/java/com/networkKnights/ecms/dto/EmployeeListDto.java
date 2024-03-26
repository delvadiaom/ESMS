package com.networkKnights.ecms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class EmployeeListDto {
    private String employeeCode;
    private String employeeName;
    private String teamName;
    private String roleName;
    private String empType;
    private String experience;
    private String ctc;
}
