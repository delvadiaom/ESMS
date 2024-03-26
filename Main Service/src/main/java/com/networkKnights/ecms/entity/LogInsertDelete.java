package com.networkKnights.ecms.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Table(name = "ecms_log_insert_delete",schema = "dbo")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
public class LogInsertDelete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id",nullable = false)
    private long logId;
    @Column(name = "admin_code",nullable = false)
    private String adminCode;
    @Column(name = "emp_code",nullable = false)
    private String empCode;
    @Column(name = "action",nullable = false)
    private String action;
    @Column(name = "time_stamp",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date timeStamp;


    public LogInsertDelete(String adminCode, String empCode, String action, Date timeStamp) {
        this.adminCode = adminCode;
        this.empCode = empCode;
        this.action = action;
        this.timeStamp = timeStamp;
    }
}
