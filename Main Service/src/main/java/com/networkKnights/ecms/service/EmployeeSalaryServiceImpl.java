package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.*;
import com.networkKnights.ecms.dto.EmployeeSalaryDto;
import com.networkKnights.ecms.entity.*;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.util.EmployeeDetailsConstant;
import com.networkKnights.ecms.util.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSalaryServiceImpl implements EmployeeSalaryService {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private EmployeeSalaryDao employeeSalaryDao;
    @Autowired
    private LogInsertDeleteDao logInsertDeleteDao;
    @Autowired
    private EmployeeDetailsDao employeeDetailsDao;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private LogUpdateDao logUpdateDao;
    @Autowired
    private Helper helper;


    @Override
    public ResponseEntity<?> getAllEmployeeSalary() {
        List<EmployeeSalary> employeeSalaryList = employeeSalaryDao.findAll();

        if (employeeSalaryList.isEmpty())
            return new ResponseEntity<>(new ESMSError("No data found"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(employeeSalaryList, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeSalary(long empId) {
        if (!employeeSalaryDao.existsByEmpId(empId))
            return new ResponseEntity<>(new ESMSError("No employee of id " + empId + " found"), HttpStatus.NOT_FOUND);

        List<EmployeeSalary> employeeSalary = employeeSalaryDao.findByEmpId(empId);

        if (employeeSalary.isEmpty())
            return new ResponseEntity<>(new ESMSError("No employee of id " + empId + " found"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(employeeSalary, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> saveEmployeeSalary(String adminEmpCode,EmployeeSalaryDto employeeSalaryDTO) {
        if (!employeeDetailsDao.existsById(employeeSalaryDTO.getEmpId()))
            return new ResponseEntity<>(new ESMSError("No details of employee of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);

        if (employeeSalaryDao.existsByEmpId(employeeSalaryDTO.getEmpId()))
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeSalaryDTO.getEmpId() + " already exist"), HttpStatus.BAD_REQUEST);

        EmployeeSalary employeeSalary = this.modelMapper.map(employeeSalaryDTO, EmployeeSalary.class);

        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(employeeSalaryDTO.getEmpId());

        if(!employeeDetailsOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of employee of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        EmployeeDetails employeeDetails = employeeDetailsOptional.get();
        long departmentId = employeeDetails.getDepartmentId();
        long teamId = employeeDetails.getTeamId();

        Optional<Team> teamOptional = teamDao.findById(teamId);
        Optional<Department> departmentOptional = departmentDao.findById(departmentId);

        if(!teamOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of team of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        if(!departmentOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of department of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        Team team = teamOptional.get();
        Department department = departmentOptional.get();

        team.setCosting(helper.ChangeCosting(team.getCosting(), employeeSalary.getCtc(),"add"));
        department.setCosting(helper.ChangeCosting(department.getCosting(), employeeSalary.getCtc(),"add"));

        team.setModifiedDate(new Date());
        department.setModifiedDate(new Date());

        teamDao.save(team);
        departmentDao.save(department);

        logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode, employeeDetails.getEmpCode(), "insert", new Date()));
        employeeSalary.setCreatedDate(new Date());
        employeeSalary.setModifiedDate(new Date());
        employeeSalaryDao.save(employeeSalary);
        return new ResponseEntity<>(employeeSalary, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateEmployeeSalary(String adminEmpCode,EmployeeSalaryDto employeeSalaryDTO) {
        if (!employeeSalaryDao.existsByEmpId(employeeSalaryDTO.getEmpId()))
            return new ResponseEntity<>(new ESMSError("No details of employee of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);

        EmployeeSalary employeeSalary = this.modelMapper.map(employeeSalaryDTO, EmployeeSalary.class);

        long empId = employeeSalary.getEmpId();
        EmployeeSalary oldEmployeeSalary = employeeSalaryDao.findByEmpId(empId).get(0);

        employeeSalary.setSalaryID(oldEmployeeSalary.getSalaryID());

        List<LogUpdate> logUpdates = new ArrayList<>();

        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(employeeSalaryDTO.getEmpId());

        if(!employeeDetailsOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of employee of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        EmployeeDetails employeeDetails = employeeDetailsOptional.get();

        if (employeeSalary.getCtc().equals(oldEmployeeSalary.getCtc()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("CTC"), String.valueOf(oldEmployeeSalary.getCtc()), String.valueOf(employeeSalary.getCtc()), new Date(), employeeDetails.getEmpCode()));

        if (employeeSalary.getBasicSalary().equals(oldEmployeeSalary.getBasicSalary()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("BASIC_SALARY"), String.valueOf(oldEmployeeSalary.getBasicSalary()), String.valueOf(employeeSalary.getBasicSalary()), new Date(), employeeDetails.getEmpCode()));

        if (employeeSalary.getHra().equals(oldEmployeeSalary.getHra()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("HRA"), String.valueOf(oldEmployeeSalary.getHra()), String.valueOf(employeeSalary.getHra()), new Date(), employeeDetails.getEmpCode()));

        if (employeeSalary.getPf().equals(oldEmployeeSalary.getPf()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PF"), String.valueOf(oldEmployeeSalary.getPf()), String.valueOf(employeeSalary.getPf()), new Date(), employeeDetails.getEmpCode()));

        if (employeeSalary.getRaise().equals(oldEmployeeSalary.getRaise()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("RAISE"), String.valueOf(oldEmployeeSalary.getRaise()), String.valueOf(employeeSalary.getRaise()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeSalary.getPayrollCode().equals(oldEmployeeSalary.getPayrollCode()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PAYROLL_CODE"), String.valueOf(oldEmployeeSalary.getPayrollCode()), String.valueOf(employeeSalary.getPayrollCode()), new Date(), employeeDetails.getEmpCode()));

        long departmentId = employeeDetails.getDepartmentId();
        long teamId = employeeDetails.getTeamId();

        Optional<Team> teamOptional = teamDao.findById(teamId);
        Optional<Department> departmentOptional = departmentDao.findById(departmentId);

        if(!teamOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of team of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        if(!departmentOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of department of id " + employeeSalaryDTO.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        Team team = teamOptional.get();
        Department department = departmentOptional.get();

        team.setCosting(helper.ChangeCosting(team.getCosting(),oldEmployeeSalary.getCtc(),"remove"));
        department.setCosting(helper.ChangeCosting(department.getCosting(),oldEmployeeSalary.getCtc(),"remove"));
        team.setCosting(helper.ChangeCosting(team.getCosting(), employeeSalary.getCtc(),"add"));
        department.setCosting(helper.ChangeCosting(department.getCosting(), employeeSalary.getCtc(),"add"));
        team.setModifiedDate(new Date());
        department.setModifiedDate(new Date());

        teamDao.save(team);
        departmentDao.save(department);

        employeeSalary.setModifiedDate(new Date());
        employeeSalaryDao.save(employeeSalary);
        logUpdateDao.saveAll(logUpdates);
        return new ResponseEntity<>(employeeSalary, HttpStatus.ACCEPTED);
    }
}
