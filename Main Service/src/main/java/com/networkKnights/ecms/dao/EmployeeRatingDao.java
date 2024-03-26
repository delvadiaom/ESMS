package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.EmployeeRating;
import com.networkKnights.ecms.entity.EmployeeSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRatingDao extends JpaRepository<EmployeeRating,Long> {
    List<EmployeeRating> findByEmpId(long empId);
    boolean existsByEmpId(long empId);
}
