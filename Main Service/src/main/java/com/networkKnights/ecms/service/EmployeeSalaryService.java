package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dto.EmployeeSalaryDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeSalaryService {
    ResponseEntity<?> getAllEmployeeSalary();
    ResponseEntity<?> getEmployeeSalary(long empId);
    ResponseEntity<?> saveEmployeeSalary(String adminEmpCode,EmployeeSalaryDto employeeSalaryDTO);
    ResponseEntity<?> updateEmployeeSalary(String adminEmpCode,EmployeeSalaryDto employeeSalaryDTO);
}
