package com.networkKnights.ecms.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Table(name = "ecms_dm_team",schema = "dbo")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id",nullable = false)
    private long teamId;
    @Column(name = "team_code",nullable = false,length = 100)
    private String teamCode;
    @Column(name = "team_name",nullable = false,length = 100)
    private String teamName;
    @Column(name = "lead_by",nullable = false,length = 100)
    private String leadBy;
    @Column(name = "costing",nullable = false)
    private String costing;
    @Column(name = "department_id",nullable = false)
    private long departmentId;
    @Column(name = "created_date",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;
    @Column(name = "modified_date",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;
    @Column(name = "is_active",nullable = false)
    private boolean isActive;

}
