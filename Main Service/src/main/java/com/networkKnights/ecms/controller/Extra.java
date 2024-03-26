package com.networkKnights.ecms.controller;


import com.networkKnights.ecms.constant.URLConstant;
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
//
//@RestController
//@RequestMapping(URLConstant.EMPLOYEE)
public class Extra {
//    @Autowired
//    private EmployeeDetailsService employeeDetailsService;
//
//    @GetMapping(URLConstant.GET_ALL_EMPLOYEE_DETAILS + "/{" + URLConstant.PAGE_NUMBER + "}/{" + URLConstant.PAGE_SIZE +"}")
//    public ResponseEntity<?> getAllEmployeeDetails(@PathVariable int pageNumber,@PathVariable int pageSize){return employeeDetailsService.getAllEmployeeDetails(pageNumber,pageSize);}
//    @GetMapping(URLConstant.GET_ALL_EMPLOYEE_DETAILS_BY_TEAM_ID + "/{" + URLConstant.TEAM_ID + "}")
//    public ResponseEntity<?> getAllEmployeeDetailsByTeamId(@PathVariable long teamId){ return employeeDetailsService.getAllEmployeeDetailsByTeamId(teamId); }
//    @GetMapping(URLConstant.GET_ALL_EMPLOYEE_DETAILS_BY_DEPARTMENT_ID + "/{" + URLConstant.DEPARTMENT_ID + "}")
//    public ResponseEntity<?> getAllEmployeeDetailsByDepartmentId(@PathVariable long departmentId){ return employeeDetailsService.getAllEmployeeDetailsByDepartmentId(departmentId); }
//    @GetMapping(URLConstant.GET_EMPLOYEE_DETAILS + "/{" + URLConstant.EMP_ID + "}")
//    public ResponseEntity<?> getEmployeeDetails(@PathVariable long empId){ return employeeDetailsService.getEmployeeDetails(empId); }
//    @GetMapping(URLConstant.GET_EMPLOYEE + "/{" + URLConstant.EMP_ID + "}")
//    public ResponseEntity<?> getEmployee(@PathVariable long empId){ return employeeDetailsService.getEmployee(empId); }
//    @PostMapping(URLConstant.SAVE_EMPLOYEE + "/{" + URLConstant.ADMIN_EMP_CODE + "}")
//    public ResponseEntity<?> saveEmployee(@RequestBody EmployeeDto employeeDto,@PathVariable String adminEmpCode){ return employeeDetailsService.saveEmployee(adminEmpCode,employeeDto); }
//    @PutMapping(URLConstant.UPDATE_EMPLOYEE + "/{" + URLConstant.ADMIN_EMP_CODE + "}")
//    public ResponseEntity<?> updateEmployee(@PathVariable String adminEmpCode,@RequestBody EmployeeDto employeeDto){ return employeeDetailsService.updateEmployee(adminEmpCode,employeeDto); }
//    @GetMapping(URLConstant.GET_EMPLOYEE_DETAILS_FROM_EMAIL + "/{" + URLConstant.EMAIL + "}")
//    public ResponseEntity<?> getEmployeeDetailsFromEmail(@PathVariable String email){ return employeeDetailsService.getEmployeeDetailsFromEmail(email); }
//    @PostMapping(URLConstant.SAVE_EMPLOYEE_DETAILS + "/{" + URLConstant.ADMIN_EMP_CODE + "}")
//    public ResponseEntity<?> saveEmployeeDetails(@PathVariable String adminEmpCode, @RequestBody EmployeeDetailsDto employeeDetailsDto){ return employeeDetailsService.saveEmployeeDetails(adminEmpCode,employeeDetailsDto); }
//    @PutMapping(URLConstant.UPDATE_EMPLOYEE_DETAILS + "/{" + URLConstant.ADMIN_EMP_CODE + "}")
//    public ResponseEntity<?> updateEmployeeDetails(@PathVariable String adminEmpCode,@RequestBody EmployeeDetailsDto employeeDetailsDto){ return employeeDetailsService.updateEmployeeDetails(adminEmpCode,employeeDetailsDto); }
//    @PutMapping(URLConstant.DELETE_EMPLOYEE_DETAILS + "/{" + URLConstant.ADMIN_EMP_CODE + "}")
//    public ResponseEntity<?> deleteEmployeeDetails(@PathVariable String adminEmpCode,@PathVariable long empId){ return employeeDetailsService.deleteEmployeeDetails(adminEmpCode,empId);}
//    @PutMapping(URLConstant.ACTIVE_EMPLOYEE_DETAILS + "/{" + URLConstant.ADMIN_EMP_CODE + "}")
//    public ResponseEntity<?> activeEmployeeDetails(@PathVariable String adminEmpCode,@PathVariable long empId){ return employeeDetailsService.activeEmployeeDetails(adminEmpCode,empId); }
//    @GetMapping(URLConstant.GET_EMPLOYEE_IMAGE + "/{" + URLConstant.EMP_ID + "}")
//    public ResponseEntity<?> getEmployeeProfileImg(@PathVariable long empId) { return employeeDetailsService.getEmployeeProfileImg(empId); }
//    @PostMapping(URLConstant.SAVE_EMPLOYEE_IMAGE + "/{" + URLConstant.EMP_ID + "}/{" + URLConstant.ADMIN_EMP_CODE +"}")
//    public ResponseEntity<?> saveEmployeeImage(@PathVariable String adminEmpCode,@PathVariable long empId, @RequestParam("profileImg") MultipartFile profileImg){ return employeeDetailsService.saveEmployeeImage(adminEmpCode,empId,profileImg); }
//    @PutMapping(URLConstant.UPDATE_EMPLOYEE_IMAGE + "/{" + URLConstant.EMP_ID + "}/{" + URLConstant.ADMIN_EMP_CODE +"}")
//    public ResponseEntity<?> updateEmployeeProfileImg(@PathVariable String adminEmpCode,@PathVariable long empId, @RequestParam("profileImg") MultipartFile profileImg){ return employeeDetailsService.updateEmployeeProfileImg(adminEmpCode,empId,profileImg); }
//    @GetMapping(URLConstant.GET_COUNT_OF_EMPLOYEE)
//    public ResponseEntity<?> getCountOfEmployee(){ return employeeDetailsService.getCountOfEmployee();}
//    @GetMapping(URLConstant.GET_EMPLOYEE_DETAILS_BY_NAME_CONTAINING + "/{" + URLConstant.NAME + "}")
//    public ResponseEntity<?> getEmployeeDetailsByNameContaining(@PathVariable String name){ return employeeDetailsService.getEmployeeDetailsByNameContaining(name); }
//    @GetMapping(URLConstant.GET_ACTIVE_EMPLOYEE_DETAILS)
//    public ResponseEntity<?> getActiveEmployeeDetails(){ return employeeDetailsService.getActiveEmployeeDetails();}
//    @GetMapping(URLConstant.GET_INACTIVE_EMPLOYEE_DETAILS)
//    public ResponseEntity<?> getInactiveEmployeeDetails(){ return employeeDetailsService.getInactiveEmployeeDetails(); }
//    @GetMapping(URLConstant.GET_EMPLOYEE_ROLES)
//    public ResponseEntity<?> getEmployeeRoles(){ return employeeDetailsService.getEmployeeRoles(); }
//    @PostMapping(URLConstant.SAVE_EMPLOYEE_ROLES)
//    public ResponseEntity<?> saveEmployeeRoles(@RequestBody RoleLookUpDto roleLookUpDto){ return employeeDetailsService.saveEmployeeRoles(roleLookUpDto); }
}
