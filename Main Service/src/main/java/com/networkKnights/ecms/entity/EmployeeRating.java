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
@Table(name = "ecms_employee_rating")
@DynamicInsert
@DynamicUpdate
@Data
@NoArgsConstructor
public class EmployeeRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_rating_id",nullable = false)
    private long ratingId;
    @Column(name = "emp_id",nullable = false)
    private long empId;
    @Column(name = "rating",nullable = false)
    private long rating;
    @Column(name = "modified_date",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;
    @Column(name = "created_date",nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;
    @Column(name = "is_active",nullable = false)
    private boolean isActive;

    public EmployeeRating(long empId, long rating, Date modifiedDate, Date createdDate, boolean isActive) {
        this.empId = empId;
        this.rating = rating;
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
        this.isActive = isActive;
    }
}
