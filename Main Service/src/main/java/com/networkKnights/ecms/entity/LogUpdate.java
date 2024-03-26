package com.networkKnights.ecms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.networkKnights.ecms.dto.LogUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/*
 * @Created By Harshang Akabari
 * @Date 27-March-2023
 */
@Entity
@Table(name = "ecms_log_update", schema = "dbo")
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor

public class LogUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private long logId;
    @Column(name = "admin_code", nullable = false)
    private String adminCode;
    @Column(name = "column_id", nullable = false)
    private long columnId;
    @Column(name = "old_value", nullable = false)
    private String oldValue;
    @Column(name = "new_value", nullable = false)
    private String newValue;
    @Column(name = "time_stamp", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date timeStamp;
    @Column(name = "emp_code", nullable = false)
    private String empCode;

    public LogUpdate(long logId, String adminCode, long columnId, String oldValue, String newValue, Date timeStamp,String empCode) {
        this.logId = logId;
        this.adminCode = adminCode;
        this.columnId = columnId;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.timeStamp = timeStamp;
        this.empCode = empCode;
    }
}
