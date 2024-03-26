package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.dto.EmployeeSalaryDto;
import com.networkKnights.ecms.service.EmployeeSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/employeeSalary")
public class EmployeeSalaryController {
    @Autowired
    private EmployeeSalaryService employeeSalaryService;
    @GetMapping("/getAllEmployeeSalary")
    public ResponseEntity<?> getAllEmployeeSalary(){
        return employeeSalaryService.getAllEmployeeSalary();
    }
    @GetMapping("/getEmployeeSalary/{empId}")
    public ResponseEntity<?> getEmployeeSalary(@PathVariable long empId){
        return employeeSalaryService.getEmployeeSalary(empId);
    }
    @PostMapping("/saveEmployeeSalary/{adminEmpCode}")
    public ResponseEntity<?> saveEmployeeSalary(@PathVariable String adminEmpCode,@RequestBody EmployeeSalaryDto employeeSalaryDTO){
        return employeeSalaryService.saveEmployeeSalary(adminEmpCode,employeeSalaryDTO);
    }
    @PutMapping("/updateEmployeeSalary/{adminEmpCode}")
    public ResponseEntity<?> updateEmployeeSalary(@PathVariable String adminEmpCode,@RequestBody EmployeeSalaryDto employeeSalaryDTO){
        return employeeSalaryService.updateEmployeeSalary(adminEmpCode,employeeSalaryDTO);
    }
}
