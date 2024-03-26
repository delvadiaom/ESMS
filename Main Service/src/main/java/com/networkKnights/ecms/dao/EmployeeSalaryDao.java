package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.EmployeeSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeSalaryDao extends JpaRepository<EmployeeSalary,Long> {
    Boolean existsByEmpId(long empId);
    List<EmployeeSalary> findByEmpId(long empId);
    @Query("SELECT e.ctc FROM EmployeeSalary e WHERE e.empId = :id")
    String findCtcByEmpId(@Param("id") Long id);
}
