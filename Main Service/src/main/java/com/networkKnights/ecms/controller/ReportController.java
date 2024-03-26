package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/report")
public class ReportController {
    @Autowired
    public ReportService reportService;
    /**
     * This method handles the GET request for all departments.
     * It retrieves a list of all departments along with there deptname, dcode, headby, headbyname, costing, status, ctc, active/inactive state and returns them as a JSON array.
     */
    @GetMapping("/getDepartmentReport")
    public ResponseEntity<?> getDepartmentReport()
    {
        return reportService.getDepartmentReport();
    }
    /**
     * This method handles the GET request for all employees.
     * It retrieves a list of all employees and returns them as a JSON array.
     */
    //PieChatForEmployeeExperienceWiseCTCGraph
    @GetMapping("/getEmployeeExperienceGraph")
    public ResponseEntity<?> getEmployeeExperienceGraph()
    {
        return reportService.getEmployeeExperienceGraph();
    }
    /**
     * This method handles the GET request for all employees with pagination function.
     * It retrieves a list of all employees and returns them as a JSON array.
     */
    //ListOfEmployeesWithCosting: empName, empCode, teamName, type, experience, ctc
    @GetMapping("/getEmployeeReport/{pageNumber}/{pageSize}")
    public ResponseEntity<?> getEmployeeReport(@PathVariable int pageNumber,@PathVariable int pageSize)
    {
        return reportService.getEmployeeReport(pageNumber,pageSize);
    }
    /**
     * This method handles the GET request for all teams by departmentID.
     * It retrieves a list of all teams and returns them as a JSON array.
     */
    //PieChatForDeptWiseTeamsCosting: cBim: 9300000, WorkFlow: 8020017
    @GetMapping("/getTeamGraph/{departmentID}")
    public ResponseEntity<?> getTeamGraph(@PathVariable long departmentID)
    {
        return reportService.getTeamGraph(departmentID);
    }
    /**
     * This method handles the GET request for all departments.
     * It retrieves a list of all departments and returns them as a JSON array.
     */
    //ListOfAllDept'sCostingInLastTenYears(LAST10YEARS)
    @GetMapping("/getCostingByDepartment")
    public ResponseEntity<?> getCostingByDepartment()
    {
        return reportService.getCostingByDepartment();
    }
}
