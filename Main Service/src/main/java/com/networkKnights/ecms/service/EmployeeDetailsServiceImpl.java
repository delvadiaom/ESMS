package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.*;
import com.networkKnights.ecms.dto.EmployeeDetailsDto;
import com.networkKnights.ecms.dto.EmployeeDto;
import com.networkKnights.ecms.dto.RoleLookUpDto;
import com.networkKnights.ecms.entity.*;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.util.ESMSStatus;
import com.networkKnights.ecms.util.EmployeeDetailsConstant;
import com.networkKnights.ecms.util.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.javamail.JavaMailSender;


import javax.transaction.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class EmployeeDetailsServiceImpl implements EmployeeDetailsService {

    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private TeamDao teamDao;
    @Autowired
    private EmployeeDetailsDao employeeDetailsDao;
    @Autowired
    private EmployeeSalaryDao employeeSalaryDao;
    @Autowired
    private EmployeeRatingDao employeeRatingDao;
    @Autowired
    private LogInsertDeleteDao logInsertDeleteDao;
    @Autowired
    private LogUpdateDao logUpdateDao;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleLookUpDao roleLookUpDao;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private Helper helper;
    @Autowired
    private JavaMailSender mailSender;

    private final JdbcTemplate jdbcTemplate;

    public EmployeeDetailsServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public ResponseEntity<?> getEmployee(long empId) {

        try {
            Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(empId);
            List<EmployeeSalary> employeeSalaryList = employeeSalaryDao.findByEmpId(empId);
            List<EmployeeRating> employeeRatingList = employeeRatingDao.findByEmpId(empId);

            if (!employeeDetailsOptional.isPresent())
                return new ResponseEntity<>("No employee details for id " + empId + " found", HttpStatus.NOT_FOUND);

            if (employeeSalaryList.isEmpty())
                return new ResponseEntity<>("No salary details for id " + empId + " found", HttpStatus.NOT_FOUND);

            if (employeeRatingList.isEmpty())
                return new ResponseEntity<>("No rating details for id " + empId + " found", HttpStatus.NOT_FOUND);

            EmployeeDto employeeDto = this.modelMapper.map(employeeDetailsOptional.get(), EmployeeDto.class);
            employeeDto.setDepartmentName(departmentDao.findDepartmentNameById(employeeDetailsOptional.get().getDepartmentId()));
            employeeDto.setTeamName(teamDao.findTeamNameById(employeeDetailsOptional.get().getTeamId()));
            employeeDto.setRoleName(roleLookUpDao.findRoleNameById(employeeDetailsOptional.get().getRoleId()));
            employeeDto.setEmpType(String.valueOf(employeeDetailsOptional.get().getEmpType()));

            employeeDto.setSalaryID(employeeSalaryList.get(0).getSalaryID());

            employeeDto.setCtc(encryptionService.decrypt(employeeSalaryList.get(0).getCtc()));
            employeeDto.setBasicSalary(encryptionService.decrypt(employeeSalaryList.get(0).getBasicSalary()));
            employeeDto.setHra(encryptionService.decrypt(employeeSalaryList.get(0).getHra()));
            employeeDto.setPf(encryptionService.decrypt(employeeSalaryList.get(0).getPf()));
            employeeDto.setOther(encryptionService.decrypt(employeeSalaryList.get(0).getOther()));
            employeeDto.setRaise(encryptionService.decrypt(employeeSalaryList.get(0).getRaise()));
            employeeDto.setPayrollCode(encryptionService.decrypt(employeeSalaryList.get(0).getPayrollCode()));

//            employeeDto.setCtc((employeeSalaryList.get(0).getCtc()));
//            employeeDto.setBasicSalary((employeeSalaryList.get(0).getBasicSalary()));
//            employeeDto.setHra((employeeSalaryList.get(0).getHra()));
//            employeeDto.setPf((employeeSalaryList.get(0).getPf()));
//            employeeDto.setOther((employeeSalaryList.get(0).getOther()));
//            employeeDto.setRaise((employeeSalaryList.get(0).getRaise()));
//            employeeDto.setPayrollCode((employeeSalaryList.get(0).getPayrollCode()));

            employeeDto.setRatingId(employeeRatingList.get(0).getRatingId());
            employeeDto.setRating(employeeRatingList.get(0).getRating());

            return new ResponseEntity<>(employeeDto, HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Data not found"), HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    @Override
    @Transactional
    public ResponseEntity<?> saveEmployee(String adminEmpCode, EmployeeDto employeeDto) {

        //Saving Employee Details
        if (employeeDetailsDao.existsByEmpCode(employeeDto.getEmpCode()))
            return new ResponseEntity<>(new ESMSError("Employee of code " + employeeDto.getEmpCode() + " already exist"), HttpStatus.BAD_REQUEST);

        if (employeeDetailsDao.existsByEmailId(employeeDto.getEmailId()))
            return new ResponseEntity<>(new ESMSError("Employee with email " + employeeDto.getEmailId() + " already found"), HttpStatus.BAD_REQUEST);


        employeeDto.setFullName(employeeDto.getFullName().toLowerCase());
        employeeDto.setReportingManager(employeeDto.getReportingManager().toLowerCase());
        employeeDto.setPermanentAddress(employeeDto.getPermanentAddress().toLowerCase());
        employeeDto.setPresentAddress(employeeDto.getPresentAddress().toLowerCase());

        EmployeeDetails employeeDetails = this.modelMapper.map(employeeDto, EmployeeDetails.class);

        employeeDetails.setCreatedDate(new Date());
        employeeDetails.setModifiedDate(new Date());
        employeeDetails.setIsActive(true);
        System.out.println(employeeDetails.toString());

        try{
            long empId = employeeDetailsDao.save(employeeDetails).getEmpId();
            System.out.println(employeeDetails.toString());

            //Saving Employee Salary Details
            if (!employeeDetailsDao.existsById(empId))
                return new ResponseEntity<>(new ESMSError("No details of employee of id " + empId + " found"), HttpStatus.NOT_FOUND);

            if (employeeSalaryDao.existsByEmpId(empId))
                return new ResponseEntity<>(new ESMSError("Employee of id " + empId + " already exist"), HttpStatus.BAD_REQUEST);

            EmployeeSalary employeeSalary = new EmployeeSalary(empId, employeeDto.getCtc(), employeeDto.getBasicSalary(), employeeDto.getHra(), employeeDto.getPf(), employeeDto.getOther(), employeeDto.getRaise(), new Date(), new Date(), true, employeeDto.getPayrollCode());

            long departmentId = employeeDetails.getDepartmentId();
            long teamId = employeeDetails.getTeamId();

            Optional<Team> teamOptional = teamDao.findById(teamId);
            Optional<Department> departmentOptional = departmentDao.findById(departmentId);

            if (!teamOptional.isPresent()) {
                return new ResponseEntity<>(new ESMSError("No details of team of id " + teamId + " found"), HttpStatus.NOT_FOUND);
            }

            if (!departmentOptional.isPresent()) {
                return new ResponseEntity<>(new ESMSError("No details of department of id " + departmentId + " found"), HttpStatus.NOT_FOUND);
            }

            Team team = teamOptional.get();
            Department department = departmentOptional.get();

            team.setCosting(helper.ChangeCosting(team.getCosting(), employeeSalary.getCtc(), "add"));
            department.setCosting(helper.ChangeCosting(department.getCosting(), employeeSalary.getCtc(), "add"));

            team.setModifiedDate(new Date());
            department.setModifiedDate(new Date());

            teamDao.save(team);
            departmentDao.save(department);


            //Saving Employee Rating Details
            EmployeeRating employeeRating = new EmployeeRating(empId, employeeDto.getRating(), new Date(), new Date(), true);

            if (employeeRatingDao.existsByEmpId(empId)) {
                return new ResponseEntity<>(new ESMSError("Employee of id " + empId + " already exists"), HttpStatus.BAD_REQUEST);
            }

            employeeDto.setEmpId(empId);
            System.out.println(employeeRating.toString());
            System.out.println(employeeDetails.toString());
            System.out.println(employeeSalary.toString());

            employeeSalary.setCtc(encryptionService.encrypt(employeeSalary.getCtc()));
            employeeSalary.setBasicSalary(encryptionService.encrypt(employeeSalary.getBasicSalary()));
            employeeSalary.setPf(encryptionService.encrypt(employeeSalary.getPf()));
            employeeSalary.setHra(encryptionService.encrypt(employeeSalary.getHra()));
            employeeSalary.setRaise(encryptionService.encrypt(employeeSalary.getRaise()));
            employeeSalary.setOther(encryptionService.encrypt(employeeSalary.getOther()));
            employeeSalary.setPayrollCode(encryptionService.encrypt(employeeSalary.getPayrollCode()));

            employeeRatingDao.save(employeeRating);
            employeeSalaryDao.save(employeeSalary);
            logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode, employeeDetails.getEmpCode(), "insert", new Date()));
            return getEmployee(empId);
        }catch (Exception e){
            return new ResponseEntity<>(new ESMSError("Data not found"), HttpStatus.SERVICE_UNAVAILABLE);

        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateEmployee(String adminEmpCode, EmployeeDto employeeDto) {

        if (!employeeSalaryDao.existsByEmpId(employeeDto.getEmpId()))
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);


        if (!employeeRatingDao.existsByEmpId(employeeDto.getEmpId()))
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);

        long empId = employeeDto.getEmpId();
        List<EmployeeRating> oldEmployeeRatingList = employeeRatingDao.findByEmpId(empId);
        List<EmployeeSalary> oldEmployeeSalaryList = employeeSalaryDao.findByEmpId(empId);
        Optional<EmployeeDetails> oldEmployeeDetailsOptional = employeeDetailsDao.findById(empId);

        if (!oldEmployeeDetailsOptional.isPresent())
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);

        if (oldEmployeeSalaryList.isEmpty())
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);

        if (oldEmployeeRatingList.isEmpty())
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);

        EmployeeDetails oldEmployeeDetails = oldEmployeeDetailsOptional.get();
        EmployeeSalary oldEmployeeSalary = oldEmployeeSalaryList.get(0);

        oldEmployeeSalary.setCtc(encryptionService.decrypt(oldEmployeeSalary.getCtc()));
        oldEmployeeSalary.setBasicSalary(encryptionService.decrypt(oldEmployeeSalary.getBasicSalary()));
        oldEmployeeSalary.setHra(encryptionService.decrypt(oldEmployeeSalary.getHra()));
        oldEmployeeSalary.setPf(encryptionService.decrypt(oldEmployeeSalary.getPf()));
        oldEmployeeSalary.setOther(encryptionService.decrypt(oldEmployeeSalary.getOther()));
        oldEmployeeSalary.setRaise(encryptionService.decrypt(oldEmployeeSalary.getRaise()));
        oldEmployeeSalary.setPayrollCode(encryptionService.decrypt(oldEmployeeSalary.getPayrollCode()));

        EmployeeRating oldEmployeeRating = oldEmployeeRatingList.get(0);

        EmployeeDetails newEmployeeDetails = this.modelMapper.map(employeeDto, EmployeeDetails.class);
        newEmployeeDetails.setCreatedDate(oldEmployeeDetails.getCreatedDate());
        newEmployeeDetails.setIsActive(oldEmployeeDetails.getIsActive());
        newEmployeeDetails.setModifiedDate(new Date());

        EmployeeSalary newEmployeeSalary = new EmployeeSalary(empId, employeeDto.getCtc(), employeeDto.getBasicSalary(), employeeDto.getHra(), employeeDto.getPf(), employeeDto.getOther(), employeeDto.getRaise(), oldEmployeeSalary.getCreatedDate(), new Date(), true, employeeDto.getPayrollCode());
        newEmployeeSalary.setSalaryID(employeeDto.getSalaryID());

        EmployeeRating newEmployeeRating = new EmployeeRating(empId, employeeDto.getRating(), new Date(), oldEmployeeRating.getCreatedDate(), true);
        newEmployeeRating.setRatingId(employeeDto.getRatingId());

//        System.out.println(newEmployeeDetails.toString());
//        System.out.println(newEmployeeSalary.toString());
//        System.out.println(newEmployeeRating.toString());

        boolean isCtcChanged = false;
        if (!oldEmployeeDetails.getIsActive()) {
            return new ResponseEntity<>(new ESMSError("Employee is in-active"), HttpStatus.BAD_REQUEST);
        }

//        You can't change empCode
        if (!newEmployeeDetails.getEmpCode().equals(oldEmployeeDetails.getEmpCode()))
            return new ResponseEntity<>(new ESMSError("Employee code can't be changed"), HttpStatus.BAD_REQUEST);

        List<LogUpdate> logUpdates = new ArrayList<>();

        String oldCtc = null;
        String newCtc = null;

        if (!newEmployeeDetails.getEmailId().equals(oldEmployeeDetails.getEmailId())) {
            if (employeeDetailsDao.existsByEmailId(newEmployeeDetails.getEmailId()))
                return new ResponseEntity<>("Employee with email " + employeeDto.getEmailId() + " already found", HttpStatus.ACCEPTED);

            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("EMAIL_ID"), oldEmployeeDetails.getFullName(), newEmployeeDetails.getFullName(), new Date(), newEmployeeDetails.getEmpCode()));
        }

        if (employeeDto.getRating() != oldEmployeeRating.getRating())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("RATING"), String.valueOf(oldEmployeeRating.getRating()), String.valueOf(employeeDto.getRating()), new Date(), employeeDto.getEmpCode()));

        if (!newEmployeeSalary.getCtc().equals(oldEmployeeSalary.getCtc())) {

            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("CTC"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getCtc())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getCtc())), new Date(), employeeDto.getEmpCode()));

            long departmentId = oldEmployeeDetails.getDepartmentId();
            long teamId = oldEmployeeDetails.getTeamId();

            Optional<Team> teamOptional = teamDao.findById(teamId);
            Optional<Department> departmentOptional = departmentDao.findById(departmentId);

            if (!teamOptional.isPresent()) {
                return new ResponseEntity<>(new ESMSError("No details of team of id " + employeeDto.getEmpId() + " found"), HttpStatus.NOT_FOUND);
            }

            if (!departmentOptional.isPresent()) {
                return new ResponseEntity<>(new ESMSError("No details of department of id " + employeeDto.getEmpId() + " found"), HttpStatus.NOT_FOUND);
            }

            Team team = teamOptional.get();
            Department department = departmentOptional.get();

            team.setCosting(helper.ChangeCosting(team.getCosting(), oldEmployeeSalary.getCtc(), "remove"));
            department.setCosting(helper.ChangeCosting(department.getCosting(), oldEmployeeSalary.getCtc(), "remove"));
            team.setCosting(helper.ChangeCosting(team.getCosting(), newEmployeeSalary.getCtc(), "add"));
            department.setCosting(helper.ChangeCosting(department.getCosting(), newEmployeeSalary.getCtc(), "add"));
            team.setModifiedDate(new Date());
            department.setModifiedDate(new Date());

            oldCtc = oldEmployeeSalary.getCtc();
            newCtc = newEmployeeSalary.getCtc();
            isCtcChanged = true;

            teamDao.save(team);
            departmentDao.save(department);
        }

        if (!newEmployeeSalary.getBasicSalary().equals(oldEmployeeSalary.getBasicSalary()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("BASIC_SALARY"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getBasicSalary())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getBasicSalary())), new Date(), employeeDto.getEmpCode()));

        if (!newEmployeeSalary.getHra().equals(oldEmployeeSalary.getHra()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("HRA"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getHra())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getHra())), new Date(), employeeDto.getEmpCode()));

        if (!newEmployeeSalary.getPf().equals(oldEmployeeSalary.getPf()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PF"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getPf())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getPf())), new Date(), employeeDto.getEmpCode()));

        if (!newEmployeeSalary.getOther().equals(oldEmployeeSalary.getOther()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("OTHER"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getOther())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getOther())), new Date(), employeeDto.getEmpCode()));

        if (!newEmployeeSalary.getRaise().equals(oldEmployeeSalary.getRaise()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("RAISE"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getRaise())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getRaise())), new Date(), employeeDto.getEmpCode()));

        if (!newEmployeeSalary.getPayrollCode().equals(oldEmployeeSalary.getPayrollCode()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PAYROLL_CODE"), encryptionService.encrypt(String.valueOf(oldEmployeeSalary.getPayrollCode())), encryptionService.encrypt(String.valueOf(newEmployeeSalary.getPayrollCode())), new Date(), employeeDto.getEmpCode()));


        newEmployeeSalary.setCtc(encryptionService.encrypt(newEmployeeSalary.getCtc()));
        newEmployeeSalary.setBasicSalary(encryptionService.encrypt(newEmployeeSalary.getBasicSalary()));
        newEmployeeSalary.setPf(encryptionService.encrypt(newEmployeeSalary.getPf()));
        newEmployeeSalary.setHra(encryptionService.encrypt(newEmployeeSalary.getHra()));
        newEmployeeSalary.setRaise(encryptionService.encrypt(newEmployeeSalary.getRaise()));
        newEmployeeSalary.setOther(encryptionService.encrypt(newEmployeeSalary.getOther()));
        newEmployeeSalary.setPayrollCode(encryptionService.encrypt(newEmployeeSalary.getPayrollCode()));
        newEmployeeSalary.setCreatedDate(oldEmployeeSalary.getCreatedDate());
        newEmployeeSalary.setModifiedDate(oldEmployeeSalary.getModifiedDate());
        newEmployeeSalary.setModifiedDate(new Date());
        employeeSalaryDao.save(newEmployeeSalary);

        if (!newEmployeeDetails.getFullName().equals(oldEmployeeDetails.getFullName()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("FULL_NAME"), oldEmployeeDetails.getFullName(), newEmployeeDetails.getFullName(), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getGender().equals(oldEmployeeDetails.getGender()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("GENDER"), oldEmployeeDetails.getGender(), newEmployeeDetails.getGender(), new Date(), newEmployeeDetails.getEmpCode()));

        if (newEmployeeDetails.getRoleId() != oldEmployeeDetails.getRoleId())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("ROLE_ID"), String.valueOf(oldEmployeeDetails.getRoleId()), String.valueOf(newEmployeeDetails.getRoleId()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getReportingManager().equals(oldEmployeeDetails.getReportingManager()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("REPORTING_MANAGER"), String.valueOf(oldEmployeeDetails.getReportingManager()), String.valueOf(newEmployeeDetails.getReportingManager()), new Date(), newEmployeeDetails.getEmpCode()));

        if (newEmployeeDetails.getEmpType() != oldEmployeeDetails.getEmpType())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("EMP_TYPE"), String.valueOf(oldEmployeeDetails.getEmpType()), String.valueOf(newEmployeeDetails.getEmpType()), new Date(), newEmployeeDetails.getEmpCode()));

        if (newEmployeeDetails.getActualEmployementDate().compareTo(oldEmployeeDetails.getActualEmployementDate()) != 0)
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("ACTUAL_EMPLOYEMENT_DATE"), String.valueOf(oldEmployeeDetails.getActualEmployementDate()), String.valueOf(newEmployeeDetails.getActualEmployementDate()), new Date(), newEmployeeDetails.getEmpCode()));

        if (newEmployeeDetails.getPastExperience() != oldEmployeeDetails.getPastExperience())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PAST_EXPERIENCE"), String.valueOf(oldEmployeeDetails.getPastExperience()), String.valueOf(newEmployeeDetails.getPastExperience()), new Date(), newEmployeeDetails.getEmpCode()));

        if (newEmployeeDetails.getDateOfJoining().compareTo(oldEmployeeDetails.getDateOfJoining()) != 0)
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("DATE_OF_JOINING"), String.valueOf(oldEmployeeDetails.getDateOfJoining()), String.valueOf(newEmployeeDetails.getDateOfJoining()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getPermanentAddress().equals(oldEmployeeDetails.getPermanentAddress()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PERMANENT_ADDRESS"), String.valueOf(oldEmployeeDetails.getPermanentAddress()), String.valueOf(newEmployeeDetails.getPermanentAddress()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getPresentAddress().equals(oldEmployeeDetails.getPresentAddress()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PRESENT_ADDRESS"), String.valueOf(oldEmployeeDetails.getPresentAddress()), String.valueOf(newEmployeeDetails.getPermanentAddress()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getPhoneNumber().equals(oldEmployeeDetails.getPhoneNumber()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PHONE_NUMBER"), String.valueOf(oldEmployeeDetails.getPhoneNumber()), String.valueOf(newEmployeeDetails.getPhoneNumber()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getEmergencyNumber().equals(oldEmployeeDetails.getEmergencyNumber()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("EMERGENCY_NUMBER"), String.valueOf(oldEmployeeDetails.getEmergencyNumber()), String.valueOf(newEmployeeDetails.getEmergencyNumber()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getPanNo().equals(oldEmployeeDetails.getPanNo()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PAN_NO"), String.valueOf(oldEmployeeDetails.getPanNo()), String.valueOf(newEmployeeDetails.getPanNo()), new Date(), newEmployeeDetails.getEmpCode()));

        if (!newEmployeeDetails.getAadharNo().equals(oldEmployeeDetails.getAadharNo()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("AADHAR_NO"), String.valueOf(oldEmployeeDetails.getAadharNo()), String.valueOf(newEmployeeDetails.getAadharNo()), new Date(), newEmployeeDetails.getEmpCode()));

        if ((oldEmployeeDetails.getDateOfBirth()).compareTo(newEmployeeDetails.getDateOfBirth()) != 0)
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("DATE_OF_BIRTH"), String.valueOf(oldEmployeeDetails.getDateOfBirth()), String.valueOf(newEmployeeDetails.getDateOfBirth()), new Date(), newEmployeeDetails.getEmpCode()));

        if (newEmployeeDetails.getIsActive() != oldEmployeeDetails.getIsActive())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("IS_ACTIVE"), String.valueOf(oldEmployeeDetails.getIsActive()), String.valueOf(newEmployeeDetails.getIsActive()), new Date(), newEmployeeDetails.getEmpCode()));


        if (newEmployeeDetails.getDepartmentId() != oldEmployeeDetails.getDepartmentId()) {
            if (oldEmployeeDetails.getTeamId() == newEmployeeDetails.getTeamId()) {
                return new ResponseEntity<>(new ESMSError("No Team of id " + newEmployeeDetails.getTeamId() + " exist under department of id " + newEmployeeDetails.getDepartmentId()), HttpStatus.NOT_FOUND);
            }
        }

        if (newEmployeeDetails.getTeamId() != oldEmployeeDetails.getTeamId()) {
            long newTeamId = newEmployeeDetails.getTeamId();
            long oldTeamId = oldEmployeeDetails.getTeamId();

            //Team exists by teamId from DTO
            if (!teamDao.existsById(newEmployeeDetails.getTeamId()))
                return new ResponseEntity<>(new ESMSError("No Team of id " + employeeDto.getTeamId() + " found"), HttpStatus.NOT_FOUND);

            //Team from Database
            Team newTeam = teamDao.findById(newEmployeeDetails.getTeamId()).get();
            //Department of that newTeam
            long teamDepartment = newTeam.getDepartmentId();

            //Team from Dto is under the correct department
            if (teamDepartment != newEmployeeDetails.getDepartmentId())
                return new ResponseEntity<>(new ESMSError("No Team of id " + employeeDto.getTeamId() + " exist under department of id " + newEmployeeDetails.getDepartmentId()), HttpStatus.NOT_FOUND);


            String ctc = employeeDto.getCtc();

            //Change Team Costing
            //2020:900000###2021:800000###2022:700000###2023:600000

            Team oldTeam = teamDao.findById(oldTeamId).get();
            String oldTeamCosting = helper.ChangeCosting(oldTeam.getCosting(), ctc, "remove");
            oldTeam.setCosting(oldTeamCosting);

            String newTeamCosting = helper.ChangeCosting(newTeam.getCosting(), ctc, "add");
            newTeam.setCosting(newTeamCosting);

            //Team changed and department changed
            if (oldEmployeeDetails.getDepartmentId() != newEmployeeDetails.getDepartmentId()) {
                //removing ctc from old department
                Department oldDepartment = departmentDao.findById(oldEmployeeDetails.getDepartmentId()).get();
                String oldDepartmentCosting = helper.ChangeCosting(oldDepartment.getCosting(), ctc, "remove");
                oldDepartment.setCosting(oldDepartmentCosting);

                //add ctc to new department
                Department newDepartment = departmentDao.findById(newEmployeeDetails.getDepartmentId()).get();
                String newDepartmentCosting = helper.ChangeCosting(newDepartment.getCosting(), ctc, "add");
                newDepartment.setCosting(newDepartmentCosting);

//                departmentDao.save(oldDepartment);
                departmentDao.save(newDepartment);
                logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("DEPARTMENT_ID"), String.valueOf(oldEmployeeDetails.getDepartmentId()), String.valueOf(newEmployeeDetails.getDepartmentId()), new Date(), newEmployeeDetails.getEmpCode()));
            }

//            teamDao.save(oldTeam);
            teamDao.save(newTeam);
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("TEAM_ID"), String.valueOf(oldEmployeeDetails.getTeamId()), String.valueOf(newEmployeeDetails.getTeamId()), new Date(), newEmployeeDetails.getEmpCode()));
        }

        employeeDetailsDao.save(newEmployeeDetails);
        employeeRatingDao.save(newEmployeeRating);
        logUpdateDao.saveAll(logUpdates);

        if(isCtcChanged){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(newEmployeeDetails.getEmailId());
            message.setSubject("CTC changed.");
            message.setText("Congratulations\n" + "your ctc has been changed from " + oldCtc + " to " + newCtc
                    +"\nHra :"+encryptionService.decrypt(newEmployeeSalary.getHra())
                    +"\nPf :"+encryptionService.decrypt(newEmployeeSalary.getHra())
                    +"\nbasic salary:"+encryptionService.decrypt(newEmployeeSalary.getBasicSalary())
                    +"\nraise:"+encryptionService.decrypt(newEmployeeSalary.getRaise()));
            mailSender.send(message);

//            System.out.println("Congratulations\n" + "your ctc has been changed from " + oldCtc + "\nto" + newCtc + ".");
        }
        return getEmployee(empId);
    }

    @Override
    public ResponseEntity<?> getAllEmployeeDetails(int pageNumber, int pageSize) {
        List<EmployeeDetailsDto> employees = jdbcTemplate.query("{call getEmployeeInBatch(?,?,?)}", new Object[]{pageNumber,pageSize,-1},
                (resultSet, rowNum) -> {
                    EmployeeDetailsDto employee = new EmployeeDetailsDto();
                    employee.setEmpId(resultSet.getLong("emp_id"));
                    employee.setAadharNo(resultSet.getString("aadhar_no"));
                    employee.setCreatedDate(resultSet.getDate("created_date"));
                    employee.setActualEmployementDate(resultSet.getDate("actual_employement_date"));
                    employee.setDateOfBirth(resultSet.getDate("date_of_birth"));
                    employee.setDateOfJoining(resultSet.getDate("date_of_joining"));
                    employee.setDepartmentId(resultSet.getLong("department_id"));
                    employee.setEmailId(resultSet.getString("email_id"));
                    employee.setEmergencyNumber(resultSet.getString("emergency_number"));
                    employee.setEmpCode(resultSet.getString("emp_code"));
                    employee.setFullName(resultSet.getString("full_name"));
                    employee.setGender(resultSet.getString("gender"));
                    employee.setIsActive(resultSet.getBoolean("is_active"));
                    employee.setModifiedDate(resultSet.getDate("modified_date"));
                    employee.setPanNo(resultSet.getString("pan_no"));
                    employee.setPastExperience(String.valueOf(resultSet.getInt("past_experience")));
                    employee.setPermanentAddress(resultSet.getString("permanent_address"));
                    employee.setPhoneNumber(resultSet.getString("phone_number"));
                    employee.setPresentAddress(resultSet.getString("present_address"));
                    employee.setProfileImage(resultSet.getString("profile_image"));
                    employee.setReportingManager(resultSet.getString("reporting_manager"));
                    employee.setRoleId(resultSet.getLong("role_id"));
                    employee.setTeamId(resultSet.getLong("team_id"));
                    employee.setDepartmentName(resultSet.getString("department_name"));
                    employee.setTeamName(resultSet.getString("team_name"));
                    employee.setRoleName(resultSet.getString("role_name"));
                    employee.setEmpType(EmployeeDetailsConstant.EmployeeType.get(resultSet.getInt("emp_type")));
                    return employee;
                });
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeDetails(long empId) {
        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(empId);
        if (!employeeDetailsOptional.isPresent()) {
            return new ResponseEntity<>(employeeDetailsDao.findById(empId), HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(employeeDetailsDao.findById(empId), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeDetailsFromEmail(String email) {
        List<EmployeeDetails> employeeDetailsList = employeeDetailsDao.findByEmailId(email);
        if (employeeDetailsList.isEmpty())
            return new ResponseEntity<>(new ESMSError("No user with email " + email + " found"), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(employeeDetailsList.get(0), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getAllEmployeeDetailsByTeamId(long teamId) {
        if (!teamDao.existsById(teamId))
            return new ResponseEntity<>(new ESMSError("No team with " + teamId + " found"), HttpStatus.NOT_FOUND);


        List<EmployeeDetails> employeeDetailsList = employeeDetailsDao.findByTeamId(teamId);

        List<EmployeeDetailsDto> employeeDetailsDtoList = new ArrayList<EmployeeDetailsDto>();

        for (EmployeeDetails employeeDetails : employeeDetailsList) {
            EmployeeDetailsDto employeeDetailsDto = this.modelMapper.map(employeeDetails, EmployeeDetailsDto.class);

            employeeDetailsDto.setDepartmentName(departmentDao.findDepartmentNameById(employeeDetails.getDepartmentId()));

            employeeDetailsDto.setTeamName(teamDao.findTeamNameById(employeeDetails.getTeamId()));

            employeeDetailsDto.setRoleName(roleLookUpDao.findRoleNameById(employeeDetails.getRoleId()));

            employeeDetailsDto.setEmpType(EmployeeDetailsConstant.EmployeeType.get((int) employeeDetails.getEmpType()));

            employeeDetailsDtoList.add(employeeDetailsDto);
        }

        return new ResponseEntity<>(employeeDetailsDtoList, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getAllEmployeeDetailsByDepartmentId(long departmentId) {
        if (!departmentDao.existsById(departmentId))
            return new ResponseEntity<>(new ESMSError("No department with " + departmentId + " found"), HttpStatus.NOT_FOUND);


        List<EmployeeDetails> employeeDetailsList = employeeDetailsDao.findByDepartmentId(departmentId);

        List<EmployeeDetailsDto> employeeDetailsDtoList = new ArrayList<EmployeeDetailsDto>();

        for (EmployeeDetails employeeDetails : employeeDetailsList) {
            EmployeeDetailsDto employeeDetailsDto = this.modelMapper.map(employeeDetails, EmployeeDetailsDto.class);

            employeeDetailsDto.setDepartmentName(departmentDao.findDepartmentNameById(employeeDetails.getDepartmentId()));

            employeeDetailsDto.setTeamName(teamDao.findTeamNameById(employeeDetails.getTeamId()));

            employeeDetailsDto.setRoleName(roleLookUpDao.findRoleNameById(employeeDetails.getRoleId()));

            employeeDetailsDto.setEmpType(EmployeeDetailsConstant.EmployeeType.get((int) employeeDetails.getEmpType()));

            employeeDetailsDtoList.add(employeeDetailsDto);
        }

        return new ResponseEntity<>(employeeDetailsDtoList, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> saveEmployeeDetails(String adminEmpCode, EmployeeDetailsDto employeeDetailsDto) {
        if (employeeDetailsDao.existsByEmpCode(employeeDetailsDto.getEmpCode()))
            return new ResponseEntity<>(new ESMSError("Employee of code " + employeeDetailsDto.getEmpCode() + " already exist"), HttpStatus.BAD_REQUEST);

        if (employeeDetailsDao.existsByEmailId(employeeDetailsDto.getEmailId()))
            return new ResponseEntity<>(new ESMSError("Employee with email " + employeeDetailsDto.getEmailId() + " already found"), HttpStatus.BAD_REQUEST);

        employeeDetailsDto.setFullName(employeeDetailsDto.getFullName().toLowerCase());
        employeeDetailsDto.setReportingManager(employeeDetailsDto.getReportingManager().toLowerCase());
        employeeDetailsDto.setPermanentAddress(employeeDetailsDto.getPermanentAddress().toLowerCase());
        employeeDetailsDto.setPresentAddress(employeeDetailsDto.getPresentAddress().toLowerCase());

        EmployeeDetails employeeDetails = this.modelMapper.map(employeeDetailsDto, EmployeeDetails.class);

        employeeDetails.setCreatedDate(new Date());
        employeeDetails.setModifiedDate(new Date());
        employeeDetailsDao.save(employeeDetails);
        logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode, employeeDetails.getEmpCode(), "insert", new Date()));
        return new ResponseEntity<>("Saved", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateEmployeeDetails(String adminEmpCode, EmployeeDetailsDto employeeDetailsDto) {
        System.out.println(employeeDetailsDto.toString());
        EmployeeDetails employeeDetails = modelMapper.map(employeeDetailsDto, EmployeeDetails.class);

        long empId = employeeDetails.getEmpId();
        Optional<EmployeeDetails> oldEmployeeOptional = employeeDetailsDao.findById(empId);

        if (!oldEmployeeOptional.isPresent()) {
            return new ResponseEntity<>(new ESMSError("No employee of id " + employeeDetailsDto.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        EmployeeDetails oldEmployeeDetails = oldEmployeeOptional.get();

        if (!oldEmployeeDetails.getIsActive()) {
            return new ResponseEntity<>(new ESMSError("Employee is in-active"), HttpStatus.BAD_REQUEST);
        }

//        You can't change empCode
        if (!employeeDetails.getEmpCode().equals(oldEmployeeDetails.getEmpCode()))
            return new ResponseEntity<>(new ESMSError("Employee code can't be changed"), HttpStatus.BAD_REQUEST);


        if (employeeDetails.equals(oldEmployeeDetails)) {
            return new ResponseEntity<>(new ESMSError("Employee details are not changed"), HttpStatus.BAD_REQUEST);
        }


//        From Here Updating and logging will be started
        List<LogUpdate> logUpdates = new ArrayList<>();

        if (!employeeDetails.getEmailId().equals(oldEmployeeDetails.getEmailId())) {
            if (employeeDetailsDao.existsByEmailId(employeeDetails.getEmailId()))
                return new ResponseEntity<>("Employee with email " + employeeDetailsDto.getEmailId() + " already found", HttpStatus.ACCEPTED);

            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("EMAIL_ID"), oldEmployeeDetails.getFullName(), employeeDetails.getFullName(), new Date(), employeeDetails.getEmpCode()));
        }

        if (employeeDetails.getDepartmentId() != oldEmployeeDetails.getDepartmentId()) {
            if (oldEmployeeDetails.getTeamId() == employeeDetails.getTeamId()) {
                return new ResponseEntity<>(new ESMSError("No Team of id " + employeeDetailsDto.getTeamId() + " exist under department of id " + employeeDetailsDto.getDepartmentId()), HttpStatus.NOT_FOUND);
            }
        }

        if (employeeDetails.getTeamId() != oldEmployeeDetails.getTeamId()) {
            long newTeamId = employeeDetails.getTeamId();
            long oldTeamId = oldEmployeeDetails.getTeamId();

            //Team exists by teamId from DTO
            if (!teamDao.existsById(employeeDetails.getTeamId()))
                return new ResponseEntity<>(new ESMSError("No Team of id " + employeeDetailsDto.getTeamId() + " found"), HttpStatus.NOT_FOUND);

            //Team from Database
            Team newTeam = teamDao.findById(employeeDetails.getTeamId()).get();
            //Department of that newTeam
            long teamDepartment = newTeam.getDepartmentId();

            //Team from Dto is under the correct department
            if (teamDepartment != employeeDetails.getDepartmentId())
                return new ResponseEntity<>(new ESMSError("No Team of id " + employeeDetailsDto.getTeamId() + " exist under department of id " + employeeDetails.getDepartmentId()), HttpStatus.NOT_FOUND);

            Optional<EmployeeSalary> employeeSalaryOptional = employeeSalaryDao.findById(empId);

            if (!employeeSalaryOptional.isPresent())
                return new ResponseEntity<>(new ESMSError("No employee information of salary of id " + employeeDetailsDto.getTeamId() + " found" + employeeDetails.getDepartmentId()), HttpStatus.NOT_FOUND);


            String ctc = employeeSalaryOptional.get().getCtc();

            //Change Team Costing
            //2020:900000###2021:800000###2022:700000###2023:600000

            Team oldTeam = teamDao.findById(oldTeamId).get();
            String oldTeamCosting = helper.ChangeCosting(oldTeam.getCosting(), ctc, "remove");
            oldTeam.setCosting(oldTeamCosting);

            String newTeamCosting = helper.ChangeCosting(newTeam.getCosting(), ctc, "add");
            newTeam.setCosting(newTeamCosting);

            //Team changed and department changed
            if (oldEmployeeDetails.getDepartmentId() != employeeDetails.getDepartmentId()) {
                //removing ctc from old department
                Department oldDepartment = departmentDao.findById(oldEmployeeDetails.getDepartmentId()).get();
                String oldDepartmentCosting = helper.ChangeCosting(oldDepartment.getCosting(), ctc, "remove");
                oldDepartment.setCosting(oldDepartmentCosting);

                //add ctc to new department
                Department newDepartment = departmentDao.findById(employeeDetails.getDepartmentId()).get();
                String newDepartmentCosting = helper.ChangeCosting(newDepartment.getCosting(), ctc, "add");
                newDepartment.setCosting(newDepartmentCosting);

                departmentDao.save(oldDepartment);
                departmentDao.save(newDepartment);
                logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("DEPARTMENT_ID"), String.valueOf(oldEmployeeDetails.getDepartmentId()), String.valueOf(employeeDetails.getDepartmentId()), new Date(), employeeDetails.getEmpCode()));
            }

            teamDao.save(oldTeam);
            teamDao.save(newTeam);
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("TEAM_ID"), String.valueOf(oldEmployeeDetails.getTeamId()), String.valueOf(employeeDetails.getTeamId()), new Date(), employeeDetails.getEmpCode()));
        }

        if (!employeeDetails.getFullName().equals(oldEmployeeDetails.getFullName()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("FULL_NAME"), oldEmployeeDetails.getFullName(), employeeDetails.getFullName(), new Date(), employeeDetails.getEmpCode()));


        if (!employeeDetails.getGender().equals(oldEmployeeDetails.getGender()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("GENDER"), oldEmployeeDetails.getGender(), employeeDetails.getGender(), new Date(), employeeDetails.getEmpCode()));

        if (employeeDetails.getRoleId() != oldEmployeeDetails.getRoleId())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("ROLE_ID"), String.valueOf(oldEmployeeDetails.getRoleId()), String.valueOf(employeeDetails.getRoleId()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getReportingManager().equals(oldEmployeeDetails.getReportingManager()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("REPORTING_MANAGER"), String.valueOf(oldEmployeeDetails.getReportingManager()), String.valueOf(employeeDetails.getReportingManager()), new Date(), employeeDetails.getEmpCode()));

        if (employeeDetails.getEmpType() != oldEmployeeDetails.getEmpType())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("EMP_TYPE"), String.valueOf(oldEmployeeDetails.getEmpType()), String.valueOf(employeeDetails.getEmpType()), new Date(), employeeDetails.getEmpCode()));

        if (employeeDetails.getActualEmployementDate().compareTo(oldEmployeeDetails.getActualEmployementDate()) != 0)
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("ACTUAL_EMPLOYEMENT_DATE"), String.valueOf(oldEmployeeDetails.getActualEmployementDate()), String.valueOf(employeeDetails.getActualEmployementDate()), new Date(), employeeDetails.getEmpCode()));

        if (employeeDetails.getPastExperience() != oldEmployeeDetails.getPastExperience())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PAST_EXPERIENCE"), String.valueOf(oldEmployeeDetails.getPastExperience()), String.valueOf(employeeDetails.getPastExperience()), new Date(), employeeDetails.getEmpCode()));

        if (employeeDetails.getDateOfJoining().compareTo(oldEmployeeDetails.getDateOfJoining()) != 0)
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("DATE_OF_JOINING"), String.valueOf(oldEmployeeDetails.getDateOfJoining()), String.valueOf(employeeDetails.getDateOfJoining()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getPermanentAddress().equals(oldEmployeeDetails.getPermanentAddress()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PERMANENT_ADDRESS"), String.valueOf(oldEmployeeDetails.getPermanentAddress()), String.valueOf(employeeDetails.getPermanentAddress()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getPresentAddress().equals(oldEmployeeDetails.getPresentAddress()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PRESENT_ADDRESS"), String.valueOf(oldEmployeeDetails.getPresentAddress()), String.valueOf(employeeDetails.getPermanentAddress()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getPhoneNumber().equals(oldEmployeeDetails.getPhoneNumber()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PHONE_NUMBER"), String.valueOf(oldEmployeeDetails.getPhoneNumber()), String.valueOf(employeeDetails.getPhoneNumber()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getEmergencyNumber().equals(oldEmployeeDetails.getEmergencyNumber()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("EMERGENCY_NUMBER"), String.valueOf(oldEmployeeDetails.getEmergencyNumber()), String.valueOf(employeeDetails.getEmergencyNumber()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getPanNo().equals(oldEmployeeDetails.getPanNo()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PAN_NO"), String.valueOf(oldEmployeeDetails.getPanNo()), String.valueOf(employeeDetails.getPanNo()), new Date(), employeeDetails.getEmpCode()));

        if (!employeeDetails.getAadharNo().equals(oldEmployeeDetails.getAadharNo()))
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("AADHAR_NO"), String.valueOf(oldEmployeeDetails.getAadharNo()), String.valueOf(employeeDetails.getAadharNo()), new Date(), employeeDetails.getEmpCode()));

        if ((oldEmployeeDetails.getDateOfBirth()).compareTo(employeeDetails.getDateOfBirth()) != 0)
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("DATE_OF_BIRTH"), String.valueOf(oldEmployeeDetails.getDateOfBirth()), String.valueOf(employeeDetails.getDateOfBirth()), new Date(), employeeDetails.getEmpCode()));

        if (employeeDetails.getIsActive() != oldEmployeeDetails.getIsActive())
            logUpdates.add(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("IS_ACTIVE"), String.valueOf(oldEmployeeDetails.getIsActive()), String.valueOf(employeeDetails.getIsActive()), new Date(), employeeDetails.getEmpCode()));

        employeeDetails.setModifiedDate(new Date());
        employeeDetailsDao.save(employeeDetails);
        logUpdateDao.saveAll(logUpdates);

        return new ResponseEntity<>(employeeDetails, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> deleteEmployeeDetails(String adminEmpCode, long empId) {

        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(empId);
        if (!employeeDetailsOptional.isPresent())
            return new ResponseEntity<>(new ESMSError("No employee of id " + empId + " found"), HttpStatus.NOT_FOUND);

        EmployeeDetails employeeDetails = employeeDetailsOptional.get();

        List<EmployeeSalary> employeeSalaryList = employeeSalaryDao.findByEmpId(empId);
        List<EmployeeRating> employeeRatingList = employeeRatingDao.findByEmpId(empId);

            employeeDetails.setIsActive(false);

            //Making employee inactive from the salary table
            if (!employeeSalaryList.isEmpty()) {
                EmployeeSalary employeeSalary = employeeSalaryDao.findByEmpId(empId).get(0);

                long departmentId = employeeDetails.getDepartmentId();
                long teamId = employeeDetails.getTeamId();

                Optional<Team> teamOptional = teamDao.findById(teamId);
                Optional<Department> departmentOptional = departmentDao.findById(departmentId);

                if (!teamOptional.isPresent()) {
                    return new ResponseEntity<>(new ESMSError("No details of team of id " + employeeSalary.getEmpId() + " found"), HttpStatus.NOT_FOUND);
                }

                if (!departmentOptional.isPresent()) {
                    return new ResponseEntity<>(new ESMSError("No details of department of id " + employeeSalary.getEmpId() + " found"), HttpStatus.NOT_FOUND);
                }

                Team team = teamOptional.get();
                Department department = departmentOptional.get();

                team.setCosting(helper.ChangeCosting(team.getCosting(), encryptionService.decrypt(employeeSalary.getCtc()), "remove"));
                department.setCosting(helper.ChangeCosting(department.getCosting(), encryptionService.decrypt(employeeSalary.getCtc()), "remove"));

                team.setModifiedDate(new Date());
                department.setModifiedDate(new Date());

                teamDao.save(team);
                departmentDao.save(department);

                employeeSalary.setActive(false);
                employeeSalary.setModifiedDate(new Date());
                employeeSalaryDao.save(employeeSalary);
            }

            //Making employee inactive from the rating table
            if (!employeeRatingList.isEmpty()) {
                EmployeeRating employeeRating = employeeRatingDao.findByEmpId(empId).get(0);
                employeeRating.setActive(false);
                employeeRating.setModifiedDate(new Date());
                employeeRatingDao.save(employeeRating);
            }

            employeeDetails.setProfileImage("default.jpg");
            employeeDetails.setModifiedDate(new Date());
            employeeDetailsDao.save(employeeDetails);
            logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode, employeeDetails.getEmpCode(), "delete", new Date()));
            return new ResponseEntity<>(new ESMSStatus("Deleted successfully"), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> activeEmployeeDetails(String adminEmpCode, long empId) {

        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(empId);
        if (!employeeDetailsOptional.isPresent())
            return new ResponseEntity<>(new ESMSError("No employee of id " + empId + " found"), HttpStatus.NOT_FOUND);

        EmployeeDetails employeeDetails = employeeDetailsOptional.get();

        List<EmployeeSalary> employeeSalaryList = employeeSalaryDao.findByEmpId(empId);
        List<EmployeeRating> employeeRatingList = employeeRatingDao.findByEmpId(empId);

        employeeDetails.setIsActive(true);

        //Making employee active from the salary table
        if (!employeeSalaryList.isEmpty()) {
            EmployeeSalary employeeSalary = employeeSalaryDao.findByEmpId(empId).get(0);

            long departmentId = employeeDetails.getDepartmentId();
            long teamId = employeeDetails.getTeamId();

            Optional<Team> teamOptional = teamDao.findById(teamId);
            Optional<Department> departmentOptional = departmentDao.findById(departmentId);

            if (!teamOptional.isPresent()) {
                return new ResponseEntity<>(new ESMSError("No details of team of id " + employeeSalary.getEmpId() + " found"), HttpStatus.NOT_FOUND);
            }

            if (!departmentOptional.isPresent()) {
                return new ResponseEntity<>(new ESMSError("No details of department of id " + employeeSalary.getEmpId() + " found"), HttpStatus.NOT_FOUND);
            }

            Team team = teamOptional.get();
            Department department = departmentOptional.get();

            team.setCosting(helper.ChangeCosting(team.getCosting(), encryptionService.decrypt(employeeSalary.getCtc()), "add"));
            department.setCosting(helper.ChangeCosting(department.getCosting(), encryptionService.decrypt(employeeSalary.getCtc()), "add"));

            team.setModifiedDate(new Date());
            department.setModifiedDate(new Date());

            teamDao.save(team);
            departmentDao.save(department);

            employeeSalary.setActive(true);
            employeeSalary.setModifiedDate(new Date());
            employeeSalaryDao.save(employeeSalary);
        }

        //Making employee active from the rating table
        if (!employeeRatingList.isEmpty()) {
            EmployeeRating employeeRating = employeeRatingDao.findByEmpId(empId).get(0);
            employeeRating.setActive(true);
            employeeRating.setModifiedDate(new Date());
            employeeRatingDao.save(employeeRating);
        }

        employeeDetails.setModifiedDate(new Date());
        employeeDetailsDao.save(employeeDetails);
        logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode, employeeDetails.getEmpCode(), "Insert", new Date()));
        return new ResponseEntity<>(new ESMSStatus("activated successfully"), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getEmployeeProfileImg(long id) {
        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(id);
        if (employeeDetailsOptional.isPresent()) {
            EmployeeDetails employeeDetails = employeeDetailsOptional.get();
            File directory = new File("ProfileImageResource");
            Path path = Paths.get(directory.getAbsolutePath() + File.separator + employeeDetails.getProfileImage());
            try {
                byte[] imageData = Files.readAllBytes(path);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageData, headers, HttpStatus.ACCEPTED);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> saveEmployeeImage(String adminEmpCode, long empId, MultipartFile profileImg) {
        if (!profileImg.getContentType().startsWith("image/"))
            
            return new ResponseEntity<>(new ESMSError("Not a valid image"), HttpStatus.NOT_ACCEPTABLE);

        // 150KB in byte
        long maxSize = 150 * 1024;

        if (profileImg.getSize() > maxSize)
            return new ResponseEntity<>(new ESMSError("The selected image file exceeds the maximum allowed size of 150KB. Please select a smaller file."), HttpStatus.INSUFFICIENT_STORAGE);


        try {
            File directory = new File("ProfileImageResource");
            if (!directory.exists())
                directory.mkdirs();


            //Get Employee From Database
            Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(empId);

            if (!employeeDetailsOptional.isPresent())
                return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);

            EmployeeDetails employeeDetails = employeeDetailsOptional.get();

            String fileName = profileImg.getOriginalFilename();
            int dot = fileName.lastIndexOf(".");
            String ext = fileName.substring(dot, fileName.length());

            Path path = Paths.get(directory.getAbsolutePath() + File.separator + employeeDetails.getEmpCode() + ext);
            Files.write(path, profileImg.getBytes());

            //Saving path in Database
            employeeDetails.setProfileImage(employeeDetails.getEmpCode() + ext);
            employeeDetailsDao.save(employeeDetails);
            logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode, employeeDetails.getEmpCode(), "insert", new Date()));

            return new ResponseEntity<>(new ESMSStatus("Img Saved"), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError("Failed to upload img"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateEmployeeProfileImg(String adminEmpCode, long empId, MultipartFile profileImg) {
        if (!employeeDetailsDao.existsById(empId))
            return new ResponseEntity<>(new ESMSError("Not User with id " + empId + " found"), HttpStatus.INTERNAL_SERVER_ERROR);

        if (!profileImg.getContentType().startsWith("image/"))
            return new ResponseEntity<>(new ESMSError("Not a valid image"), HttpStatus.NOT_ACCEPTABLE);

        // 150KB in byte
        long maxSize = 150 * 1024;

        if (profileImg.getSize() > maxSize)
            return new ResponseEntity<>(new ESMSError("The selected image file exceeds the maximum allowed size of 150KB. Please select a smaller file."), HttpStatus.INSUFFICIENT_STORAGE);


        try {
            File directory = new File("ProfileImageResource");
            if (!directory.exists())
                directory.mkdirs();


            //Get Employee From Database
            Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(empId);

            if (!employeeDetailsOptional.isPresent())
                return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);

            EmployeeDetails employeeDetails = employeeDetailsOptional.get();

            String fileName = profileImg.getOriginalFilename();
            int dot = fileName.lastIndexOf(".");
            String ext = fileName.substring(dot, fileName.length());

            //Saving image to ProfileImgResource
            Path path = Paths.get(directory.getAbsolutePath() + File.separator + employeeDetails.getEmpCode() + ext);
            Files.write(path, profileImg.getBytes());

            //Saving path in Database
            String oldProfileImg = employeeDetails.getProfileImage();
            employeeDetails.setProfileImage(employeeDetails.getEmpCode() + ext);

            employeeDetailsDao.save(employeeDetails);
            logUpdateDao.save(new LogUpdate(0, adminEmpCode, EmployeeDetailsConstant.arr.indexOf("PROFILE_IMAGE"), oldProfileImg, employeeDetails.getEmpCode() + ext, new Date(), employeeDetails.getEmpCode()));

            return new ResponseEntity<>(new ESMSStatus("Img Saved"), HttpStatus.ACCEPTED);

        } catch (Exception e) {
            return new ResponseEntity<>(new ESMSError(e + "Failed to upload img"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getCountOfEmployee() {
        return new ResponseEntity<>(employeeDetailsDao.count(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeDetailsByNameContaining(String name) {
        List<EmployeeDetails> employeeDetailsList = employeeDetailsDao.findEmployeeDetailsByNameContaining(name);
        List<EmployeeDetailsDto> employeeDetailsDtoList = new ArrayList<>();
        for(EmployeeDetails employeeDetails:employeeDetailsList){
            EmployeeDetailsDto employeeDetailsDto = this.modelMapper.map(employeeDetails,EmployeeDetailsDto.class);

            employeeDetailsDto.setDepartmentName(departmentDao.findDepartmentNameById(employeeDetails.getDepartmentId()));
            employeeDetailsDto.setTeamName(teamDao.findTeamNameById(employeeDetails.getTeamId()));
            employeeDetailsDto.setRoleName(roleLookUpDao.findRoleNameById(employeeDetails.getRoleId()));
            employeeDetailsDto.setEmpType(String.valueOf(employeeDetails.getEmpType()));

            employeeDetailsDtoList.add(employeeDetailsDto);
        }
        return new ResponseEntity<>(employeeDetailsDtoList,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getActiveEmployeeDetails() {
        List<EmployeeDetailsDto> employees = jdbcTemplate.query("{call getEmployeeInBatch(?,?,?)}", new Object[]{1,Integer.MAX_VALUE,1},
                (resultSet, rowNum) -> {
                    EmployeeDetailsDto employee = new EmployeeDetailsDto();
                    employee.setEmpId(resultSet.getLong("emp_id"));
                    employee.setAadharNo(resultSet.getString("aadhar_no"));
                    employee.setCreatedDate(resultSet.getDate("created_date"));
                    employee.setActualEmployementDate(resultSet.getDate("actual_employement_date"));
                    employee.setDateOfBirth(resultSet.getDate("date_of_birth"));
                    employee.setDateOfJoining(resultSet.getDate("date_of_joining"));
                    employee.setDepartmentId(resultSet.getLong("department_id"));
                    employee.setEmailId(resultSet.getString("email_id"));
                    employee.setEmergencyNumber(resultSet.getString("emergency_number"));
                    employee.setEmpCode(resultSet.getString("emp_code"));
                    employee.setFullName(resultSet.getString("full_name"));
                    employee.setGender(resultSet.getString("gender"));
                    employee.setIsActive(resultSet.getBoolean("is_active"));
                    employee.setModifiedDate(resultSet.getDate("modified_date"));
                    employee.setPanNo(resultSet.getString("pan_no"));
                    employee.setPastExperience(String.valueOf(resultSet.getInt("past_experience")));
                    employee.setPermanentAddress(resultSet.getString("permanent_address"));
                    employee.setPhoneNumber(resultSet.getString("phone_number"));
                    employee.setPresentAddress(resultSet.getString("present_address"));
                    employee.setProfileImage(resultSet.getString("profile_image"));
                    employee.setReportingManager(resultSet.getString("reporting_manager"));
                    employee.setRoleId(resultSet.getLong("role_id"));
                    employee.setTeamId(resultSet.getLong("team_id"));
                    employee.setDepartmentName(resultSet.getString("department_name"));
                    employee.setTeamName(resultSet.getString("team_name"));
                    employee.setRoleName(resultSet.getString("role_name"));
                    employee.setEmpType(EmployeeDetailsConstant.EmployeeType.get(resultSet.getInt("emp_type")));
                    return employee;
                });
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getInactiveEmployeeDetails() {
        List<EmployeeDetailsDto> employees = jdbcTemplate.query("{call getEmployeeInBatch(?,?,?)}", new Object[]{1,Integer.MAX_VALUE,0},
                (resultSet, rowNum) -> {
                    EmployeeDetailsDto employee = new EmployeeDetailsDto();
                    employee.setEmpId(resultSet.getLong("emp_id"));
                    employee.setAadharNo(resultSet.getString("aadhar_no"));
                    employee.setCreatedDate(resultSet.getDate("created_date"));
                    employee.setActualEmployementDate(resultSet.getDate("actual_employement_date"));
                    employee.setDateOfBirth(resultSet.getDate("date_of_birth"));
                    employee.setDateOfJoining(resultSet.getDate("date_of_joining"));
                    employee.setDepartmentId(resultSet.getLong("department_id"));
                    employee.setEmailId(resultSet.getString("email_id"));
                    employee.setEmergencyNumber(resultSet.getString("emergency_number"));
                    employee.setEmpCode(resultSet.getString("emp_code"));
                    employee.setFullName(resultSet.getString("full_name"));
                    employee.setGender(resultSet.getString("gender"));
                    employee.setIsActive(resultSet.getBoolean("is_active"));
                    employee.setModifiedDate(resultSet.getDate("modified_date"));
                    employee.setPanNo(resultSet.getString("pan_no"));
                    employee.setPastExperience(String.valueOf(resultSet.getInt("past_experience")));
                    employee.setPermanentAddress(resultSet.getString("permanent_address"));
                    employee.setPhoneNumber(resultSet.getString("phone_number"));
                    employee.setPresentAddress(resultSet.getString("present_address"));
                    employee.setProfileImage(resultSet.getString("profile_image"));
                    employee.setReportingManager(resultSet.getString("reporting_manager"));
                    employee.setRoleId(resultSet.getLong("role_id"));
                    employee.setTeamId(resultSet.getLong("team_id"));
                    employee.setDepartmentName(resultSet.getString("department_name"));
                    employee.setTeamName(resultSet.getString("team_name"));
                    employee.setRoleName(resultSet.getString("role_name"));
                    employee.setEmpType(EmployeeDetailsConstant.EmployeeType.get(resultSet.getInt("emp_type")));
                    return employee;
                });
        return new ResponseEntity<>(employees, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeRoles() {
        return new ResponseEntity<>(roleLookUpDao.findAll(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> saveEmployeeRoles(RoleLookUpDto roleLookUpDto) {
        RoleLookUp roleLookUp = new RoleLookUp();
        roleLookUp.setRoleName(roleLookUpDto.getRoleName());
        roleLookUp.setCreatedDate(new Date());
        roleLookUp.setModifiedDate(new Date());
        roleLookUpDao.save(roleLookUp);
        return new ResponseEntity<>(roleLookUp, HttpStatus.ACCEPTED);
    }
}
