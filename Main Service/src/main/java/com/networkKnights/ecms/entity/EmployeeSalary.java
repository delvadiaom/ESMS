package com.networkKnights.ecms.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ecms_employee_salary", schema = "dbo")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
public class EmployeeSalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_salary_id", nullable = false)
    private long salaryID;
    @Column(name = "emp_id", nullable = false)
    private long empId;
    @Column(name = "ctc", nullable = false)
    private String ctc;
    @Column(name = "basic_salary", nullable = false)
    private String basicSalary;
    @Column(name = "hra", nullable = false)
    private String hra;
    @Column(name = "pf", nullable = false)
    private String pf;
    @Column(name = "other", nullable = false)
    private String other;
    @Column(name = "raise", nullable = false)
    private String raise;
    @Column(name = "modified_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date modifiedDate;
    @Column(name = "created_date", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date createdDate;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Column(name = "payroll_code", nullable = false, length = 100)
    private String payrollCode;

    public EmployeeSalary() {
    }

    public EmployeeSalary(long empId, String ctc, String basicSalary, String hra, String pf, String other, String raise, Date modifiedDate, Date createdDate, boolean isActive, String payrollCode) {
        this.empId = empId;
        this.ctc = ctc;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.pf = pf;
        this.other = other;
        this.raise = raise;
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
        this.isActive = isActive;
        this.payrollCode = payrollCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeSalary that = (EmployeeSalary) o;
        return empId == that.empId && Objects.equals(ctc, that.ctc) && Objects.equals(basicSalary, that.basicSalary) && Objects.equals(hra, that.hra) && Objects.equals(pf, that.pf) && Objects.equals(other, that.other) && Objects.equals(raise, that.raise) && Objects.equals(payrollCode, that.payrollCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empId, ctc, basicSalary, hra, pf, other, raise, payrollCode);
    }
}
