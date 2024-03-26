package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.*;
import com.networkKnights.ecms.dto.EmployeeRatingDto;
import com.networkKnights.ecms.entity.*;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.util.EmployeeDetailsConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EmployeeRatingServiceImpl implements EmployeeRatingService {

    @Autowired
    private EmployeeRatingDao employeeRatingDao;
    @Autowired
    private EmployeeSalaryDao employeeSalaryDao;
    @Autowired
    private EmployeeDetailsDao employeeDetailsDao;
    @Autowired
    private LogInsertDeleteDao logInsertDeleteDao;
    @Autowired
    private LogUpdateDao logUpdateDao;
    @Autowired
    private ModelMapper ModelMapper;

    @Override
    public ResponseEntity<?> getAllEmployeeRating() {
        return new ResponseEntity<>(employeeRatingDao.findAll(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getEmployeeRating(long empId) {
        return new ResponseEntity<>(employeeRatingDao.findByEmpId(empId), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> saveEmployeeRating(String adminEmpCode, EmployeeRatingDto employeeRatingDto) {
        if(!employeeSalaryDao.existsByEmpId(employeeRatingDto.getEmpId())){
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeRatingDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);
        }

        if(employeeRatingDao.existsByEmpId(employeeRatingDto.getEmpId())){
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeRatingDto.getEmpId() + " already exists"), HttpStatus.BAD_REQUEST);
        }

        EmployeeRating employeeRating = this.ModelMapper.map(employeeRatingDto,EmployeeRating.class);
        employeeRating.setCreatedDate(new Date());
        employeeRating.setModifiedDate(new Date());
        employeeRatingDao.save(employeeRating);

        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(employeeRatingDto.getEmpId());

        if(!employeeDetailsOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of employee of id " + employeeRatingDto.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        EmployeeDetails employeeDetails = employeeDetailsOptional.get();
        logInsertDeleteDao.save(new LogInsertDelete(adminEmpCode , employeeDetails.getEmpCode() , "insert" ,new Date()));
        return new ResponseEntity<>(employeeRating, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateEmployeeRating(String adminEmpCode, EmployeeRatingDto employeeRatingDto) {
        if(!employeeSalaryDao.existsByEmpId(employeeRatingDto.getEmpId()))
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeRatingDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);


        if(!employeeRatingDao.existsByEmpId(employeeRatingDto.getEmpId()))
            return new ResponseEntity<>(new ESMSError("Employee of id " + employeeRatingDto.getEmpId() + " not found"), HttpStatus.NOT_FOUND);

        EmployeeRating employeeRating = this.ModelMapper.map(employeeRatingDto,EmployeeRating.class);

        long empId = employeeRating.getEmpId();
        EmployeeRating oldEmployeeRating = employeeRatingDao.findByEmpId(empId).get(0);
        long oldRating = oldEmployeeRating.getRating();

        if(employeeRatingDto.getRating() == oldRating)
            return new ResponseEntity<>(new ESMSError("Rating wasn't changed"), HttpStatus.BAD_REQUEST);

        employeeRating.setRatingId(oldEmployeeRating.getRatingId());
        employeeRating.setCreatedDate(oldEmployeeRating.getCreatedDate());
        employeeRating.setModifiedDate(new Date());
        employeeRating.setActive(oldEmployeeRating.isActive());

        employeeRatingDao.save(employeeRating);
        Optional<EmployeeDetails> employeeDetailsOptional = employeeDetailsDao.findById(employeeRatingDto.getEmpId());

        if(!employeeDetailsOptional.isPresent()){
            return new ResponseEntity<>(new ESMSError("No details of employee of id " + employeeRatingDto.getEmpId() + " found"), HttpStatus.NOT_FOUND);
        }

        EmployeeDetails employeeDetails = employeeDetailsOptional.get();
        logUpdateDao.save(new LogUpdate(0,adminEmpCode, EmployeeDetailsConstant.arr.indexOf("RATING") , String.valueOf(oldRating) , String.valueOf(employeeRatingDto.getRating()) , new Date(), employeeDetails.getEmpCode()));
        return new ResponseEntity<>(employeeRating, HttpStatus.ACCEPTED);
    }
}
