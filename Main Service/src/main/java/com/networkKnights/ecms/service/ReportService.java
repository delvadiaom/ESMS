package com.networkKnights.ecms.service;

import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<?> getDepartmentReport();
    ResponseEntity<?> getEmployeeExperienceGraph();
    ResponseEntity<?> getEmployeeReport(int pageNumber, int pageSize);
    ResponseEntity<?> getTeamGraph(long departmentID);
    ResponseEntity<?> getCostingByDepartment();

}