package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dto.EmployeeRatingDto;
import org.springframework.http.ResponseEntity;

public interface EmployeeRatingService {
    ResponseEntity<?> getAllEmployeeRating();
    ResponseEntity<?> getEmployeeRating(long empId);
    ResponseEntity<?> saveEmployeeRating(String adminEmpCode, EmployeeRatingDto employeeRatingDto);
    ResponseEntity<?> updateEmployeeRating(String adminEmpCode,EmployeeRatingDto employeeRatingDto);
}
