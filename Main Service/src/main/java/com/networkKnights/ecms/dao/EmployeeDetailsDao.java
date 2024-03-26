package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.EmployeeDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDetailsDao extends JpaRepository<EmployeeDetails,Long> {
    List<EmployeeDetails> findByEmpCode(String empCode);
    String getFullNameByEmpCode(String code);
    long countByTeamId(long id);
    boolean existsByEmpCode(String empCode);
    boolean existsByEmailId(String emailId);
    EmployeeDetails getByEmpCode(String headedBy);
    List<EmployeeDetails> findByEmailId(String emailId);
    List<EmployeeDetails> findByTeamId(long teamId);
    List<EmployeeDetails> findByDepartmentId(long departmentId);
    Page<EmployeeDetails> findByTeamId(long teamId, Pageable pageable);
    @Query("SELECT e FROM EmployeeDetails e WHERE e.fullName LIKE %:name%")
    List<EmployeeDetails> findEmployeeDetailsByNameContaining(String name);
    @Query("SELECT e FROM EmployeeDetails e WHERE e.isActive=true")
    List<EmployeeDetails> findActiveEmployeeDetails();
    @Query("SELECT e FROM EmployeeDetails e WHERE e.isActive=false")
    List<EmployeeDetails> findInactiveEmployeeDetails();

}
