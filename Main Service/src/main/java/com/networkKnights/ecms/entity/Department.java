package com.networkKnights.ecms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/*
 * @Created By Harshang Akabari
 * @Date 27-March-2023
 */
@Entity
@Table(name = "ecms_dm_department",schema = "dbo")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Department {
    @Id
    @Column(name = "department_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long departmentId;
    @Column(name = "department_name", nullable = false)
    private String departmentName;
    @Column(name = "department_code", nullable = false)
    private String departmentCode;
    @Column(name = "skills", nullable = false)
    private String skills;
    @Column(name = "headed_by", nullable = false)
    private String headedBy;
    @Column(name = "costing", nullable = false)
    private String costing;
    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;
    @Column(name = "modified_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Department() {
    }

    public Department(long departmentId, String departmentName, String departmentCode, String skills, String headedBy, String costing, Date createdDate, Date modifiedDate, boolean isActive) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.skills = skills;
        this.headedBy = headedBy;
        this.costing = costing;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.isActive = isActive;
    }
}
