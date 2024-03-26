package com.networkKnights.ecms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class EmployeeDto {
    private long empId;
    private long departmentId;
    private String departmentName;
    private String fullName;
    private String gender;
    private String profileImage;
    private String empCode;
    private long roleId;
    private String roleName;
    private String emailId;
    private String reportingManager;
    private long teamId;
    private String teamName;
    private String empType;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date actualEmployementDate;
    private String  pastExperience;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfJoining;
    private String permanentAddress;
    private String presentAddress;
    private String phoneNumber;
    private String emergencyNumber;
    private String panNo;
    private String aadharNo;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;

    //Salary
    private long salaryID;
    private String ctc;
    private String basicSalary;
    private String hra;
    private String pf;
    private String other;
    private String raise;
    private String payrollCode;

    //rating
    private long ratingId;
    private long rating;

    //common
    private Date createdDate;
    private Date modifiedDate;
    private Boolean isActive;
}
