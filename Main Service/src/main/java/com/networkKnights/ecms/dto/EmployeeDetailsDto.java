package com.networkKnights.ecms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class EmployeeDetailsDto{
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
    private Date actualEmployementDate;
    private String  pastExperience;
    private Date dateOfJoining;
    private String permanentAddress;
    private String presentAddress;
    private String phoneNumber;
    private String emergencyNumber;
    private String panNo;
    private String aadharNo;
    private Date dateOfBirth;
    private Date createdDate;
    private Date modifiedDate;
    private Boolean isActive;
}
