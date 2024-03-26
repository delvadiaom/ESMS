package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.DepartmentDao;
import com.networkKnights.ecms.dao.EmployeeDetailsDao;
import com.networkKnights.ecms.dao.TeamDao;
import com.networkKnights.ecms.dto.DepartmentDto;
import com.networkKnights.ecms.entity.Department;
import com.networkKnights.ecms.entity.EmployeeDetails;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.entity.Team;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentDao departmentDao;
    @Autowired
    TeamDao teamDao;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EmployeeDetailsDao employeeDetailsDao;

    @Override
    public ResponseEntity<?> getDepartmentsList() {
        List<Department> departmentList = departmentDao.getDepartmentsByIsActive(true);
        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        DepartmentDto departmentDto = null;
        EmployeeDetails employeeDetails = null;
        for (Department department : departmentList) {
            try {
                departmentDto = this.modelMapper.map(department, DepartmentDto.class);
                if (departmentDto.getHeadedBy().equals("")) {
                    departmentDto.setHeadedByName("");
                } else {
                    employeeDetails = employeeDetailsDao.getByEmpCode(departmentDto.getHeadedBy());
                    if (employeeDetails == null)
                        departmentDto.setHeadedByName("");
                    else
                        departmentDto.setHeadedByName(employeeDetails.getFullName());
                }
                String cost = departmentDto.getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                departmentDto.setCosting(arr2[1]);
                departmentDtoList.add(departmentDto);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }

        if (departmentDtoList.size() > 0)
            return new ResponseEntity<>(departmentDtoList, HttpStatus.OK);

        return new ResponseEntity<>(new ESMSError("There is no department"), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> getDepartmentDetails(long departmentId) {
        Optional<Department> department = departmentDao.findById(departmentId);
        DepartmentDto departmentDto = null;
        departmentDto = this.modelMapper.map(department, DepartmentDto.class);
        EmployeeDetails employeeDetails = null;
        if (departmentDto != null) {
            try {
                String cost = departmentDto.getCosting();
                if (departmentDto.getHeadedBy().equals("")) {
                    departmentDto.setHeadedByName("");
                } else {
                    employeeDetails = employeeDetailsDao.getByEmpCode(departmentDto.getHeadedBy());
                    departmentDto.setHeadedByName(employeeDetails.getFullName());
                }
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                departmentDto.setCosting(arr2[1]);
                return new ResponseEntity<>(departmentDto, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong"), HttpStatus.NOT_FOUND);
            }
        } else
            return new ResponseEntity<>(new ESMSError("Department with given id not exists"), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> saveDepartmentDetails(DepartmentDto departmentDto) {
        departmentDto.setDepartmentName(departmentDto.getDepartmentName().toLowerCase());
        departmentDto.setSkills(departmentDto.getSkills().toLowerCase());
        if (departmentDao.existsByDepartmentCode(departmentDto.getDepartmentCode())) {
            return new ResponseEntity<>(new ESMSError("Department Code Is Already Exist"), HttpStatus.BAD_REQUEST);
        }
        try {
            if (departmentDto.getHeadedBy().equals("")) {
                departmentDto.setHeadedByName("");
            } else if (employeeDetailsDao.existsByEmpCode(departmentDto.getHeadedBy())) {
                EmployeeDetails employeeDetails = employeeDetailsDao.getByEmpCode(departmentDto.getHeadedBy());
                departmentDto.setHeadedByName(employeeDetails.getFullName());
            } else {
                return new ResponseEntity<>(new ESMSError("No employee exist"), HttpStatus.BAD_REQUEST);
            }
            departmentDto.setActive(true);
            departmentDto.setCosting(new Date().getYear() + 1900 + ":" + "0");
            Department department = this.modelMapper.map(departmentDto, Department.class);
            department.setCreatedDate(new Date());
            department.setModifiedDate(new Date());
            departmentDao.save(department);
            return new ResponseEntity<>(department, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> updateDepartmentDetails(DepartmentDto departmentDto) {
        //updatable attributes
        departmentDto.setDepartmentName(departmentDto.getDepartmentName().toLowerCase());
        //departmentDto.setDepartmentCode(departmentDto.getDepartmentCode().toLowerCase());
        //departmentDto.setHeadedBy(departmentDto.getHeadedBy().toLowerCase());
        departmentDto.setSkills(departmentDto.getSkills().toLowerCase());

        if (departmentDto.getHeadedBy().equals("")) {
            departmentDto.setHeadedByName("");
        } else if (employeeDetailsDao.existsByEmpCode(departmentDto.getHeadedBy())) {
            EmployeeDetails employeeDetails = employeeDetailsDao.getByEmpCode(departmentDto.getHeadedBy());
            departmentDto.setHeadedByName(employeeDetails.getFullName());
        } else {
            return new ResponseEntity<>(new ESMSError("No employee exist"), HttpStatus.BAD_REQUEST);
        }

        Department oldDepartment = departmentDao.findById(departmentDto.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department Not Found"));
        if (oldDepartment == null) {
            return new ResponseEntity<>(new ESMSError("Department Not Exist"), HttpStatus.BAD_REQUEST);
        }
        try {
            Department department = this.modelMapper.map(departmentDto, Department.class);
            //get old attributes of department
            department.setCreatedDate(oldDepartment.getCreatedDate());
            department.setCosting(oldDepartment.getCosting());
            department.setActive(oldDepartment.isActive());
            department.setModifiedDate(new Date());
            departmentDao.save(department);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Something Went Wrong!!"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> deleteDepartmentDetails(long departmentId) {
        Department department = departmentDao.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));

        List<Team> teams = teamDao.findByDepartmentId(departmentId);
        System.out.println(teams.size());
        try {
            for (Team team : teams) {
                team.setActive(false);
            }
            teamDao.saveAll(teams);
            department.setActive(false);
            departmentDao.save(department);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Something Went Wrong"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> getAllDepartmentsList() {
        List<Department> departmentList = departmentDao.findAll();
        List<DepartmentDto> departmentDtoList = new ArrayList<>();
        DepartmentDto departmentDto = null;
        EmployeeDetails employeeDetails = null;
        for (Department department : departmentList) {
            try {
                departmentDto = this.modelMapper.map(department, DepartmentDto.class);
                employeeDetails = employeeDetailsDao.getByEmpCode(departmentDto.getHeadedBy());
                departmentDto.setHeadedByName(employeeDetails.getFullName());
                String cost = departmentDto.getCosting();
                String[] arr = cost.split("###");
                int len = arr.length;
                String[] arr2 = arr[len - 1].split(":");
                departmentDto.setCosting(arr2[1]);
                departmentDtoList.add(departmentDto);
            } catch (Exception e) {
                return new ResponseEntity<>(new ESMSError("Something Went Wrong" + e), HttpStatus.NOT_FOUND);
            }
        }
        if (departmentDtoList.size() > 0)
            return new ResponseEntity<>(departmentDtoList, HttpStatus.OK);

        return new ResponseEntity<>(new ESMSError("There is no department"), HttpStatus.NOT_FOUND);
    }

}
