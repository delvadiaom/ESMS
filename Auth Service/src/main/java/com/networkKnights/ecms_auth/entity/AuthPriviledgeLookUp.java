package com.networkKnights.ecms_auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name="auth_priviledge_",schema = "dbo")
public class AuthPriviledgeLookUp {
    @Id
    @Column(name="priviledge_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long priviledgeId;
    @Column(name="priviledge")
    private String priviledge;
    @Column(name="priviledge_name",length = 100,unique = true)
    private String priviledgeName;
    @Column(name="created_date")
    private Date createdDate;
    @Column(name="modified_date")
    private Date modifiedDate;
}
