package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.constant.URLConstant;
import com.networkKnights.ecms.dao.DepartmentDao;
import com.networkKnights.ecms.dao.TeamDao;
import com.networkKnights.ecms.dto.DepartmentDto;
import com.networkKnights.ecms.dto.TeamDto;
import com.networkKnights.ecms.entity.Department;
import com.networkKnights.ecms.entity.EmployeeDetails;
import com.networkKnights.ecms.entity.Team;
import com.networkKnights.ecms.service.DepartmentService;
import com.networkKnights.ecms.service.EmployeeDetailsService;
import com.networkKnights.ecms.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping(URLConstant.DEPARTMENT)
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @GetMapping(URLConstant.GET_ALL_DEPARTMENTS_LIST)
    public ResponseEntity<?> getDepartmentsList()
    {
        return departmentService.getDepartmentsList();
    }
    @GetMapping(URLConstant.GET_ALL_DEPARTMENT_LIST)
    public ResponseEntity<?> getAllDepartmentsList() { return departmentService.getAllDepartmentsList(); }
    @GetMapping(URLConstant.GET_DEPARTMENT_DETAILS + "/{" + URLConstant.DEPARTMENT_ID + "}")
    public ResponseEntity<?> getDepartmentDetails(@PathVariable long departmentId) { return departmentService.getDepartmentDetails(departmentId); }
    @PostMapping(URLConstant.SAVE_DEPARTMENT_DETAILS)
    public ResponseEntity<?> saveDepartmentDetails(@RequestBody DepartmentDto departmentDto) { return departmentService.saveDepartmentDetails(departmentDto); }
    @PutMapping(URLConstant.UPDATE_DEPARTMENT_DETAILS)
    public ResponseEntity<?> updateDepartmentDetails(@RequestBody DepartmentDto departmentDto) { return departmentService.updateDepartmentDetails(departmentDto); }
    @PutMapping(URLConstant.DELETE_DEPARTMENT_DETAILS + "/{" + URLConstant.DEPARTMENT_ID + "}")
    public ResponseEntity<?> deleteDepartmentDetails(@PathVariable long departmentId) { return departmentService.deleteDepartmentDetails(departmentId); }
}
