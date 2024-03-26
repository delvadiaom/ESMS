package com.networkKnights.ecms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class EmployeeSalaryDto {
    private long salaryID;
    private long empId;
    private String ctc;
    private String basicSalary;
    private String hra;
    private String pf;
    private String other;
    private String raise;
    private Date modifiedDate;
    private Date createdDate;
    private boolean isActive;
    private String payrollCode;
}
