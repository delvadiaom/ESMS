package com.networkKnights.ecms.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ecms_employee_role_",schema = "dbo")
@Getter
@Setter
public class RoleLookUp {
    @Id
    @Column(name = "role_id", nullable = false)
    long roleId;
    @Column(name = "role_name", nullable = false)
    String roleName;
    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    Date createdDate;
    @Column(name = "modified_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    Date modifiedDate;
}
