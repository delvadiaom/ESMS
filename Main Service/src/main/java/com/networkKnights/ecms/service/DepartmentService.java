package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dto.DepartmentDto;
import org.springframework.http.ResponseEntity;

public interface DepartmentService {
    public ResponseEntity<?> getDepartmentsList();
    public ResponseEntity<?> getDepartmentDetails(long departmentId);
    public ResponseEntity<?> saveDepartmentDetails(DepartmentDto departmentDto);
    public ResponseEntity<?> updateDepartmentDetails(DepartmentDto departmentDto);
    public ResponseEntity<?> deleteDepartmentDetails(long departmentId);
    public ResponseEntity<?> getAllDepartmentsList();
}
