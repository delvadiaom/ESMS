package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.DepartmentDao;
import com.networkKnights.ecms.dao.EmployeeDetailsDao;
import com.networkKnights.ecms.dao.TeamDao;
import com.networkKnights.ecms.dto.TeamDto;
import com.networkKnights.ecms.entity.EmployeeDetails;
import com.networkKnights.ecms.entity.Team;
import com.networkKnights.ecms.util.ESMSError;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    EmployeeDetailsDao employeeDetailsDao;
    @Autowired
    @PersistenceContext
    EntityManager em;

    @Override
    public ResponseEntity<?> getTeamsByDepartmentId(long departmentID) {
        List<Team> getTeamsByDepartmentId = teamDao.getTeamsByDepartmentIdAndIsActive(departmentID, true);
        List<TeamDto> teamDtos = new ArrayList<>();
        for (Team team : getTeamsByDepartmentId) {
            try {
                String cost = team.getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                TeamDto teamDto = this.modelMapper.map(team, TeamDto.class);
                teamDto.setCosting(arr2[1]);

                if(team.getLeadBy().equals("")){
                    teamDto.setLeadByName("");
                }else{
                    EmployeeDetails employeeDetails = employeeDetailsDao.getByEmpCode(teamDto.getLeadBy());
                    if (employeeDetails == null)
                        teamDto.setLeadByName("");
                    else
                        teamDto.setLeadByName(employeeDetails.getFullName());
                }

                StoredProcedureQuery sp =
                        em.createStoredProcedureQuery("getEmployeeCountForTeam");
                sp.registerStoredProcedureParameter("paramTeamEmployeeCount", Integer.class, ParameterMode.OUT);
                sp.registerStoredProcedureParameter("paramTeamId", Integer.class, ParameterMode.IN);

                sp.setParameter("paramTeamId", (int) team.getTeamId());
                sp.execute();
                teamDto.setEmployeeCount((int) sp.getOutputParameterValue("paramTeamEmployeeCount"));

                teamDtos.add(teamDto);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }
        if (getTeamsByDepartmentId.size() > 0 && !getTeamsByDepartmentId.isEmpty())
            return new ResponseEntity<>(teamDtos, HttpStatus.OK);

        return new ResponseEntity<>(teamDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> saveTeamDetails(TeamDto teamDto) {
        //updatable data
        //teamDto.setTeamCode(teamDto.getTeamCode().toLowerCase());
        teamDto.setTeamName(teamDto.getTeamName().toLowerCase());
        //teamDto.setLeadBy(teamDto.getLeadBy().toLowerCase());
        teamDto.setDepartmentId(teamDto.getDepartmentId());
        if (!departmentDao.existsById(teamDto.getDepartmentId())) {
            return new ResponseEntity<>(new ESMSError("Department id Not Exist"), HttpStatus.BAD_REQUEST);
        }
        if (teamDao.existsByTeamCode(teamDto.getTeamCode())) {
            return new ResponseEntity<>(new ESMSError("Team Code Is Already Exist"), HttpStatus.BAD_REQUEST);
        } else {
            if (teamDto.getLeadBy().equals("") || employeeDetailsDao.existsByEmpCode(teamDto.getLeadBy())) {
                Team team = this.modelMapper.map(teamDto, Team.class);
                team.setCreatedDate(new Date());
                team.setModifiedDate(new Date());
                team.setActive(true);
                team.setCosting(new Date().getYear() + 1900 + ":" + "0");
                teamDao.save(team);
                return new ResponseEntity<>(HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ESMSError("Employee does not exist"), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Override
    public ResponseEntity<?> updateTeamDetails(TeamDto teamDto) {
        //teamDto.setTeamCode(teamDto.getTeamCode().toLowerCase());
        teamDto.setTeamName(teamDto.getTeamName().toLowerCase());
        //teamDto.setLeadBy(teamDto.getLeadBy().toLowerCase());
        //teamDto.setDepartmentId(teamDto.getDepartmentId());
        if (!departmentDao.existsById(teamDto.getDepartmentId())) {
            return new ResponseEntity<>(new ESMSError("Department id Not Exist"), HttpStatus.BAD_REQUEST);
        }
        Team oldTeam = teamDao.findById(teamDto.getTeamId()).orElseThrow(() -> new RuntimeException("Team Not Found"));
        if (oldTeam == null) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        } else {
            try {
                if(teamDto.getLeadBy().equals("") || employeeDetailsDao.existsByEmpCode(teamDto.getLeadBy())) {
                    Team newTeam = this.modelMapper.map(teamDto, Team.class);
                    newTeam.setCosting(oldTeam.getCosting());
                    newTeam.setCreatedDate(oldTeam.getCreatedDate());
                    newTeam.setModifiedDate(new Date());
                    newTeam.setActive(oldTeam.isActive());
                    teamDao.save(newTeam);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ESMSError("Employee not exist for lead"), HttpStatus.NOT_FOUND);
                }
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }
    }

    @Override
    public ResponseEntity<?> deleteTeamDetails(long teamId) {
        Optional<Team> teamOptional = teamDao.findById(teamId);
        if (teamOptional.isPresent())
            try {
                Team team = teamOptional.get();
                team.setActive(false);
                teamDao.save(team);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong"), HttpStatus.NOT_FOUND);
            }
        return new ResponseEntity<>(new ESMSError("Team Not Exist"), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getTeamById(long teamId) {
        try {
            Team team = teamDao.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
            TeamDto teamDto = this.modelMapper.map(team, TeamDto.class);
            EmployeeDetails employeeDetails = employeeDetailsDao.findByEmpCode(team.getLeadBy()).get(0);
            teamDto.setLeadByName(employeeDetails.getFullName());
            String cost = teamDto.getCosting();
            String[] arr = cost.split("###");
            int len = arr.length;
            String[] arr2 = arr[len - 1].split(":");
            teamDto.setCosting(arr2[1]);
            return new ResponseEntity<>(teamDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Team not found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getEmpCountByTeam(long departmentId) {
        try {
            List<Team> getTeamsByDepartmentId = teamDao.getTeamsByDepartmentIdAndIsActive(departmentId, true);
            Map<String, Long> count = new HashMap<String, Long>();
            for (Team team : getTeamsByDepartmentId) {
                count.put(team.getTeamName(), employeeDetailsDao.countByTeamId(team.getTeamId()));
            }
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getAllTeamsByDepartmentId(long departmentID) {
        List<Team> getTeamsByDepartmentId = teamDao.getTeamsByDepartmentId(departmentID);
        List<TeamDto> teamDtos = new ArrayList<>();
        for (Team team : getTeamsByDepartmentId) {
            try {
                String cost = team.getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                TeamDto teamDto = this.modelMapper.map(team, TeamDto.class);
                teamDto.setCosting(arr2[1]);
                EmployeeDetails employeeDetails = employeeDetailsDao.findByEmpCode(team.getLeadBy()).get(0);
                if (employeeDetails == null)
                    return new ResponseEntity<>(new ESMSError("Lead employee not exists"), HttpStatus.NOT_FOUND);
                teamDto.setLeadByName(employeeDetails.getFullName());
                teamDtos.add(teamDto);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }
        if (getTeamsByDepartmentId.size() > 0 && !getTeamsByDepartmentId.isEmpty())
            return new ResponseEntity<>(teamDtos, HttpStatus.OK);

        return new ResponseEntity<>(new ESMSError("Teams not found"), HttpStatus.BAD_REQUEST);
    }
}