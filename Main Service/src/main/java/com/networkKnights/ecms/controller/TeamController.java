package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.dto.TeamDto;
import com.networkKnights.ecms.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/team")
public class TeamController {
    @Autowired
    private TeamService teamService;


    //Active teams
    @GetMapping("/getAllTeams/{departmentID}")
    public ResponseEntity<?> getTeamsByDepartmentId(@PathVariable long departmentID) {
        return teamService.getTeamsByDepartmentId(departmentID);
    }

    //All teams
    @GetMapping("/getAllTeam/{departmentID}")
    public ResponseEntity<?> getAllTeamsByDepartmentId(@PathVariable long departmentID){
        return teamService.getAllTeamsByDepartmentId(departmentID);
    }

    @GetMapping("/getEmpCountByTeam/{departmentId}")
    public ResponseEntity<?> getEmpCountByTeam(@PathVariable long departmentId)
    {
        return teamService.getEmpCountByTeam(departmentId);
    }

    @GetMapping("/getTeamById/{teamId}")
    public  ResponseEntity<?> getTeamById(@PathVariable long teamId)
    {
        return teamService.getTeamById(teamId);
    }

    @PostMapping("/saveTeamDetails")
    public ResponseEntity<?> saveTeamDetails(@RequestBody TeamDto teamDto) {
        return teamService.saveTeamDetails(teamDto);
    }

    @PutMapping("/updateTeamDetails")
    public ResponseEntity<?> updateTeamDetails(@RequestBody TeamDto teamDto) {
        return teamService.updateTeamDetails(teamDto);
    }

    @PutMapping("/deleteTeamDetails/{teamId}")
    public ResponseEntity<?> deleteTeamDetails(@PathVariable long teamId) {
        return teamService.deleteTeamDetails(teamId);
    }
}