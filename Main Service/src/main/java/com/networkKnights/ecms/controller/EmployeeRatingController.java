package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.dto.EmployeeRatingDto;
import com.networkKnights.ecms.service.EmployeeRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/employeeRating")
public class EmployeeRatingController {

    @Autowired
    private EmployeeRatingService employeeRatingService;

    @GetMapping("/getAllEmployeeRating")
    public ResponseEntity<?> getAllEmployeeRating(){
        return employeeRatingService.getAllEmployeeRating();
    }
    @GetMapping("/getEmployeeRating/{empId}")
    public ResponseEntity<?> getEmployeeRating(@PathVariable long empId){
        return employeeRatingService.getEmployeeRating(empId);
    }
    @PostMapping("/saveEmployeeRating/{adminEmpCode}")
    public ResponseEntity<?> saveEmployeeRating(@PathVariable String adminEmpCode,@RequestBody EmployeeRatingDto employeeRatingDto){
        return employeeRatingService.saveEmployeeRating(adminEmpCode,employeeRatingDto);
    }
    @PutMapping("/updateEmployeeRating/{adminEmpCode}")
    public ResponseEntity<?> updateEmployeeRating(@PathVariable String adminEmpCode,@RequestBody EmployeeRatingDto employeeRatingDto){
        return employeeRatingService.updateEmployeeRating(adminEmpCode,employeeRatingDto);
    }
}
