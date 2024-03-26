package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.Department;
import com.networkKnights.ecms.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends JpaRepository<Department,Long> {
    Boolean existsByDepartmentCode(String teamCode);
    List<Department> getDepartmentsByIsActive(boolean status);
    @Query("SELECT d.departmentName FROM Department d WHERE d.departmentId = :id")
    String findDepartmentNameById(@Param("id") Long id);
}
