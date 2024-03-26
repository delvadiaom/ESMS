package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dto.TeamDto;
import org.springframework.http.ResponseEntity;

public interface TeamService {
    public ResponseEntity<?> getTeamsByDepartmentId(long departmentID);
    public ResponseEntity<?> saveTeamDetails(TeamDto teamDto);
    public ResponseEntity<?> updateTeamDetails(TeamDto teamDto);
    public ResponseEntity<?> deleteTeamDetails(long teamId);
    public ResponseEntity<?> getTeamById(long teamId);
    public ResponseEntity<?> getEmpCountByTeam(long departmentId);
    public ResponseEntity<?> getAllTeamsByDepartmentId(long departmentID);
}
