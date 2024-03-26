package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dto.EmployeeDetailsDto;
import com.networkKnights.ecms.dto.EmployeeDto;
import com.networkKnights.ecms.dto.RoleLookUpDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeDetailsService {
    ResponseEntity<?> getAllEmployeeDetails(int pageNumber,int pageSize);
    ResponseEntity<?> getEmployeeDetails(long empId);
    ResponseEntity<?> getEmployeeDetailsFromEmail(String email);
    ResponseEntity<?> getAllEmployeeDetailsByTeamId(long teamId);
    ResponseEntity<?> getAllEmployeeDetailsByDepartmentId(long departmentId);
    ResponseEntity<?> saveEmployeeDetails(String adminEmpCode, EmployeeDetailsDto employeeDetailsDto);
    ResponseEntity<?> deleteEmployeeDetails(String adminEmpCode,long empId);
    ResponseEntity<?> activeEmployeeDetails(String adminEmpCode, long empId);
    ResponseEntity<?> getEmployeeProfileImg(long id);
    ResponseEntity<?> updateEmployeeDetails(String adminEmpCode, EmployeeDetailsDto employeeDetailsDto);
    ResponseEntity<?> saveEmployeeImage(String adminEmpCode, long empId, MultipartFile profileImg);
    ResponseEntity<?> updateEmployeeProfileImg(String adminEmpCode, long empId, MultipartFile profileImg);
    ResponseEntity<?> getEmployee(long empId);
    ResponseEntity<?> saveEmployee(String adminEmpCode,EmployeeDto employeeDto);
    ResponseEntity<?> updateEmployee(String adminEmpCode,EmployeeDto employeeDto);
    ResponseEntity<?> getCountOfEmployee();
    ResponseEntity<?> getEmployeeDetailsByNameContaining(String name);
    ResponseEntity<?> getActiveEmployeeDetails();
    ResponseEntity<?> getInactiveEmployeeDetails();
    public ResponseEntity<?> getEmployeeRoles();
    public ResponseEntity<?> saveEmployeeRoles(RoleLookUpDto roleLookUpDto);
    //Done

}
