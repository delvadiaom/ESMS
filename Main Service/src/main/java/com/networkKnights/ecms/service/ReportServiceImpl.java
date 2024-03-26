package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.*;
import com.networkKnights.ecms.dto.*;
import com.networkKnights.ecms.entity.Department;
import com.networkKnights.ecms.entity.EmployeeDetails;
import com.networkKnights.ecms.entity.Team;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.util.EmployeeDetailsConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private DepartmentDetailsDao departmentDetailsDao;
    @Autowired
    public TeamDao teamDao;
    @Autowired
    public EmployeeDetailsDao employeeDetailsDao;
    @Autowired
    private EmployeeSalaryDao employeeSalaryDao;
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    private RoleLookUpDao roleLookUpDao;
    @Autowired
    @PersistenceContext
    EntityManager em;
    private long empId;

    @Override
    public ResponseEntity<?> getDepartmentReport() {
        List<Department> departmentList = departmentDetailsDao.getDepartmentsByIsActive(true);
        List<DepartmentReportDto> departmentDtoList = new ArrayList<>();
        DepartmentReportDto departmentReportDto = null;
        EmployeeDetails employeeDetails = null;
        for (Department department : departmentList) {
            try {
                departmentReportDto = this.modelMapper.map(department, DepartmentReportDto.class);
                employeeDetails = employeeDetailsDao.getByEmpCode(departmentReportDto.getHeadedBy());
                departmentReportDto.setHeadedByName(employeeDetails.getFullName());
                String cost = departmentReportDto.getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                departmentReportDto.setCosting(arr2[1]);
                departmentDtoList.add(departmentReportDto);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }
        if (departmentDtoList.size() > 0)
            return new ResponseEntity<>(departmentDtoList, HttpStatus.OK);

        return new ResponseEntity<>(new ESMSError("There is no department"), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> getEmployeeExperienceGraph() {
        List<ExpCtcDto> expCtcDtoList = new ArrayList<>();
        List<EmployeeDetails> employeeDetailsList = employeeDetailsDao.findAll();
        int[] ctcArr = new int[25];
        int[] countArr = new int[25];
        for (EmployeeDetails employeeDetails : employeeDetailsList) {
            int exp = new Date().getYear() - employeeDetails.getActualEmployementDate().getYear();
            int currentCTC = ctcArr[exp];
            int currentCount = countArr[exp];
            if (employeeSalaryDao.findCtcByEmpId(employeeDetails.getEmpId()) != null) {
                ctcArr[exp] = currentCTC + Integer.parseInt(employeeSalaryDao.findCtcByEmpId(employeeDetails.getEmpId()));
                countArr[exp] = currentCount++;
            }
        }
        for (int i = 0; i < ctcArr.length; i++) {
            if (countArr[i] != 0) {
                int currentAvg = ctcArr[i] / countArr[i];
                ctcArr[i] = currentAvg;
            }
        }
        for (int i = 0; i < ctcArr.length; i++) {
            expCtcDtoList.add(new ExpCtcDto(i, ctcArr[i]));
        }
        return new ResponseEntity<>(expCtcDtoList, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeReport(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<EmployeeDetails> employeeDetailsPage = employeeDetailsDao.findAll(pageable);
        List<EmployeeDetails> employeeDetailsList = employeeDetailsPage.getContent();
        List<EmployeeListDto> employeeListDtoList = new ArrayList<EmployeeListDto>();
        for (EmployeeDetails employeeDetails : employeeDetailsList) {
            EmployeeListDto employeeListDto = this.modelMapper.map(employeeDetails, EmployeeListDto.class);
            employeeListDto.setTeamName(teamDao.findTeamNameById(employeeDetails.getTeamId()));
            employeeListDto.setRoleName(roleLookUpDao.findRoleNameById(employeeDetails.getRoleId()));
            employeeListDto.setEmpType(EmployeeDetailsConstant.EmployeeType.get((int) employeeDetails.getEmpType()));
            employeeListDto.setCtc(employeeSalaryDao.findCtcByEmpId(employeeDetails.getEmpId()));
            employeeListDtoList.add(employeeListDto);
        }
        return new ResponseEntity<>(employeeListDtoList, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getTeamGraph(long departmentID) {
        List<Team> getTeamsByDepartmentId = teamDao.getTeamsByDepartmentId(departmentID);
        List<TeamGraphDto> teamDtos = new ArrayList<>();
        for (Team team : getTeamsByDepartmentId) {
            try {
                String cost = team.getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                TeamGraphDto teamGraphDto = this.modelMapper.map(team, TeamGraphDto.class);
                teamGraphDto.setCosting(arr2[1]);
                EmployeeDetails employeeDetails = employeeDetailsDao.findByEmpCode(team.getLeadBy()).get(0);
                if (employeeDetails == null)
                    return new ResponseEntity<>(new ESMSError("Lead employee not exists"), HttpStatus.NOT_FOUND);
                teamDtos.add(teamGraphDto);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }
        if (getTeamsByDepartmentId.size() > 0 && !getTeamsByDepartmentId.isEmpty())
            return new ResponseEntity<>(teamDtos, HttpStatus.OK);

        return new ResponseEntity<>(new ESMSError("Teams not found"), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getCostingByDepartment() {
        List<Department> departmentList = departmentDetailsDao.findAll();
        List<CostingOfDeptDto> costingOfDeptDtoList = new ArrayList<>();
        for (int i = 0; i < departmentList.size(); i++) {
            CostingOfDeptDto costingOfDeptDto = null;
            costingOfDeptDto = this.modelMapper.map(departmentList.get(i), CostingOfDeptDto.class);
            try {
                String cost = departmentList.get(i).getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                HashMap<String, String> map = new HashMap<>();
                for (int j = 0; j < len; j++) {
                    String val = arr[j];
                    int a = arr[j].indexOf(":");
                    map.put(val.substring(0, a), val.substring(a + 1, val.length()));
                }
                costingOfDeptDto.setCosting(map);
                costingOfDeptDtoList.add(costingOfDeptDto);

            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Some error occur"), HttpStatus.BAD_REQUEST);
            }
        }
        if (costingOfDeptDtoList.size() > 0)
            return new ResponseEntity<>(costingOfDeptDtoList, HttpStatus.OK);

        return new ResponseEntity<>(new ESMSError("There is no department"), HttpStatus.NOT_FOUND);
    }
}
