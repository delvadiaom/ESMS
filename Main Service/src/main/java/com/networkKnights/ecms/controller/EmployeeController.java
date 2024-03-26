package com.networkKnights.ecms.controller;


import com.networkKnights.ecms.dto.EmployeeDetailsDto;
import com.networkKnights.ecms.dto.EmployeeDto;
import com.networkKnights.ecms.dto.RoleLookUpDto;
import com.networkKnights.ecms.entity.EmployeeDetails;
import com.networkKnights.ecms.service.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//Get - Read
//Save - Create
//Update - Update
//Delete - Delete

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @GetMapping("/getAllEmployeeDetails/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getAllEmployeeDetails(@PathVariable int pageNumber,@PathVariable int pageSize){
        return employeeDetailsService.getAllEmployeeDetails(pageNumber,pageSize);
    }
    @GetMapping("/getAllEmployeeDetailsByTeamId/{teamId}")
    public ResponseEntity<?> getAllEmployeeDetailsByTeamId(@PathVariable long teamId){
        return employeeDetailsService.getAllEmployeeDetailsByTeamId(teamId);
    }
    @GetMapping("/getAllEmployeeDetailsByDepartmentId/{departmentId}")
    public ResponseEntity<?> getAllEmployeeDetailsByDepartmentId(@PathVariable long departmentId){
        return employeeDetailsService.getAllEmployeeDetailsByDepartmentId(departmentId);
    }
    @GetMapping("/getEmployeeDetails/{empId}")
    public ResponseEntity<?> getEmployeeDetails(@PathVariable long empId){
        return employeeDetailsService.getEmployeeDetails(empId);
    }
    @GetMapping("/getEmployee/{empId}")
    public ResponseEntity<?> getEmployee(@PathVariable long empId){
        return employeeDetailsService.getEmployee(empId);
    }
    @PostMapping("/saveEmployee/{adminEmpCode}")
    public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDto employeeDto,@PathVariable String adminEmpCode){
        return employeeDetailsService.saveEmployee(adminEmpCode,employeeDto);
    }
    @PutMapping("/updateEmployee/{adminEmpCode}")
    public ResponseEntity<?> updateEmployee(@PathVariable String adminEmpCode,@RequestBody EmployeeDto employeeDto){
        return employeeDetailsService.updateEmployee(adminEmpCode,employeeDto);
    }
    @GetMapping("/getEmployeeDetailsFromEmail/{email}")
    public ResponseEntity<?> getEmployeeDetailsFromEmail(@PathVariable String email){
        return employeeDetailsService.getEmployeeDetailsFromEmail(email);
    }
    @PostMapping("/saveEmployeeDetails/{adminEmpCode}")
    public ResponseEntity<?> saveEmployeeDetails(@PathVariable String adminEmpCode, @RequestBody EmployeeDetailsDto employeeDetailsDto){
        return employeeDetailsService.saveEmployeeDetails(adminEmpCode,employeeDetailsDto);
    }
    @PutMapping("/updateEmployeeDetails/{adminEmpCode}")
    public ResponseEntity<?> updateEmployeeDetails(@PathVariable String adminEmpCode,@RequestBody EmployeeDetailsDto employeeDetailsDto){
        return employeeDetailsService.updateEmployeeDetails(adminEmpCode,employeeDetailsDto);
    }
    @PutMapping("/deleteEmployeeDetails/{empId}/{adminEmpCode}")
    public ResponseEntity<?> deleteEmployeeDetails(@PathVariable String adminEmpCode,@PathVariable long empId){
        return employeeDetailsService.deleteEmployeeDetails(adminEmpCode,empId);
    }
    @PutMapping("/activeEmployeeDetails/{empId}/{adminEmpCode}")
    public ResponseEntity<?> activeEmployeeDetails(@PathVariable String adminEmpCode,@PathVariable long empId){
        return employeeDetailsService.activeEmployeeDetails(adminEmpCode,empId);
    }
    @GetMapping("/getEmployeeImage/{id}")
    public ResponseEntity<?> getEmployeeProfileImg(@PathVariable long id) {
        return employeeDetailsService.getEmployeeProfileImg(id);
    }
    @PostMapping("/saveEmployeeImage/{empId}/{adminEmpCode}")
    public ResponseEntity<?> saveEmployeeImage(@PathVariable String adminEmpCode,@PathVariable long empId, @RequestParam("profileImg") MultipartFile profileImg){
        return employeeDetailsService.saveEmployeeImage(adminEmpCode,empId,profileImg);
    }
    @PutMapping("/updateEmployeeImage/{empId}/{adminEmpCode}")
    public ResponseEntity<?> updateEmployeeProfileImg(@PathVariable String adminEmpCode,@PathVariable long empId, @RequestParam("profileImg") MultipartFile profileImg){
        return employeeDetailsService.updateEmployeeProfileImg(adminEmpCode,empId,profileImg);
    }
    @GetMapping("/getCountOfEmployee")
    public ResponseEntity<?> getCountOfEmployee(){
        return employeeDetailsService.getCountOfEmployee();
    }
    @GetMapping("/getEmployeeDetailsByNameContaining/{name}")
    public ResponseEntity<?> getEmployeeDetailsByNameContaining(@PathVariable String name){
        return employeeDetailsService.getEmployeeDetailsByNameContaining(name);
    }
    @GetMapping("/getActiveEmployeeDetails")
    public ResponseEntity<?> getActiveEmployeeDetails(){
        return employeeDetailsService.getActiveEmployeeDetails();
    }
    @GetMapping("/getInactiveEmployeeDetails")
    public ResponseEntity<?> getInactiveEmployeeDetails(){
        return employeeDetailsService.getInactiveEmployeeDetails();
    }
    @GetMapping("/getEmployeeRoles")
    public ResponseEntity<?> getEmployeeRoles(){
        return employeeDetailsService.getEmployeeRoles();
    }
    @PostMapping("/saveEmployeeRoles")
    public ResponseEntity<?> saveEmployeeRoles(@RequestBody RoleLookUpDto roleLookUpDto){
        return employeeDetailsService.saveEmployeeRoles(roleLookUpDto);
    }
}