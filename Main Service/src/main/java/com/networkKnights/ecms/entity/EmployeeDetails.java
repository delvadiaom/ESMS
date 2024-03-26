package com.networkKnights.ecms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name="ecms_employee",schema = "dbo")
@Getter

@Setter
public class EmployeeDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="emp_id")
    private long empId;
    @Column(name="department_id")
    private long departmentId;
    @Column(name="full_name",length = 100)
    private String fullName;
    @Column(name="gender",nullable = false)
    private String gender;
    @Column(name="profile_image",nullable = false)
    private String profileImage;
    @Column(name="emp_code",nullable = false, length = 100,unique = true)
    private String empCode;
    @Column(name="role_id",nullable = false)
    private long roleId;
    @Column(name="email_id",nullable = false, length = 100)
    private String emailId;
    @Column(name="reporting_manager",nullable = false, length = 100)
    private String reportingManager;
    @Column(name="team_id",nullable = false)
    private long teamId;
    @Column(name="emp_type",nullable = false)
    private long empType;
    @Column(name="actual_employement_date",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date actualEmployementDate;
    @Column(name="past_experience",nullable = false)
    private int  pastExperience;
    @Column(name="date_of_joining",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfJoining;
    @Column(name="permanent_address",nullable = false, length = 200)
    private String permanentAddress;
    @Column(name="present_address",nullable = false, length = 200)
    private String presentAddress;
    @Column(name="phone_number",nullable = false, length = 20)
    private String phoneNumber;
    @Column(name="emergency_number",nullable = false, length = 20)
    private String emergencyNumber;
    @Column(name="panNo",nullable = false, length = 20)
    private String panNo;
    @Column(name="aadhar_no",nullable = false, length = 20)
    private String aadharNo;
    @Column(name="date_of_birth",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    @Column(name="created_date",nullable = false)
    private Date createdDate;
    @Column(name="modified_date",nullable = false)
    private Date modifiedDate;
    @Column(name="is_active",nullable = false)
    private Boolean isActive;
    public EmployeeDetails() {
    }

    public EmployeeDetails(long empId, long departmentId, String fullName,String gender, String profileImage, String empCode, long roleId, String emailId, String reportingManager, long teamId, long empType, Date actualEmployementDate, int pastExperience, Date dateOfJoining, String permanentAddress, String presentAddress, String phoneNumber, String emergencyNumber, String panNo, String aadharNo, Date dateOfBirth, Date createdDate, Date modifiedDate, Boolean isActive) {
        this.empId = empId;
        this.departmentId = departmentId;
        this.fullName = fullName;
        this.gender=gender;
        this.profileImage = profileImage;
        this.empCode = empCode;
        this.roleId = roleId;
        this.emailId = emailId;
        this.reportingManager = reportingManager;
        this.teamId = teamId;
        this.empType = empType;
        this.actualEmployementDate = actualEmployementDate;
        this.pastExperience = pastExperience;
        this.dateOfJoining = dateOfJoining;
        this.permanentAddress = permanentAddress;
        this.presentAddress = presentAddress;
        this.phoneNumber = phoneNumber;
        this.emergencyNumber = emergencyNumber;
        this.panNo = panNo;
        this.aadharNo = aadharNo;
        this.dateOfBirth = dateOfBirth;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "EmployeeDetails{" +
                "empId=" + empId +
                ", departmentId=" + departmentId +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", empCode=" + empCode +
                ", roleId=" + roleId +
                ", emailId='" + emailId + '\'' +
                ", reportingManager='" + reportingManager + '\'' +
                ", teamId=" + teamId +
                ", empType=" + empType +
                ", actualEmployementDate=" + actualEmployementDate +
                ", pastExperience='" + pastExperience + '\'' +
                ", dateOfJoining=" + dateOfJoining +
                ", permanentAddress='" + permanentAddress + '\'' +
                ", presentAddress='" + presentAddress + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", emergencyNumber=" + emergencyNumber +
                ", panNo=" + panNo +
                ", aadharNo=" + aadharNo +
                ", dateOfBirth=" + dateOfBirth +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeDetails that = (EmployeeDetails) o;
        return departmentId == that.departmentId
            && roleId == that.roleId
            && teamId == that.teamId
            && empType == that.empType
            && pastExperience == that.pastExperience
            && fullName.equals(that.fullName)
            && gender.equals(that.gender)
            && profileImage.equals(that.profileImage)
            && reportingManager.equals(that.reportingManager)
            && permanentAddress.equals(that.permanentAddress)
            && presentAddress.equals(that.presentAddress)
            && phoneNumber.equals(that.phoneNumber)
            && emergencyNumber.equals(that.emergencyNumber)
            && panNo.equals(that.panNo)
            && aadharNo.equals(that.aadharNo)
            && ((actualEmployementDate.compareTo(that.actualEmployementDate)) == 0)
            && ((dateOfJoining.compareTo(that.dateOfJoining)) == 0)
            && ((dateOfBirth.compareTo(that.dateOfBirth)) == 0)
            && ((createdDate.compareTo(that.createdDate)) == 0)
            && (isActive.equals(that.isActive));
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, fullName, gender, profileImage, roleId, reportingManager, teamId, empType, actualEmployementDate, pastExperience, dateOfJoining, permanentAddress, presentAddress, phoneNumber, emergencyNumber, panNo, aadharNo, dateOfBirth, createdDate, modifiedDate, isActive);
    }

}
