package com.networkKnights.ecms.dao;

import com.networkKnights.ecms.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamDao extends JpaRepository<Team, Long> {

    List<Team> getTeamsByDepartmentId(long departmentID);
    List<Team> getTeamsByDepartmentIdAndIsActive(long departmentID,boolean status);
    Boolean existsByTeamCode(String teamCode);
    Boolean existsByDepartmentId(long departmentID);
    List<Team> findByDepartmentId(long id);
    @Query("SELECT t.teamName FROM Team t WHERE t.teamId = :id")
    String findTeamNameById(@Param("id") Long id);


}