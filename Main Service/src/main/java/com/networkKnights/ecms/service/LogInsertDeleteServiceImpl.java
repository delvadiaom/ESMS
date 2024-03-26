package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.EmployeeDetailsDao;
import com.networkKnights.ecms.dao.LogInsertDeleteDao;
import com.networkKnights.ecms.dto.LogDto;
import com.networkKnights.ecms.dto.LogInsertDeleteDto;
import com.networkKnights.ecms.util.ESMSError;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogInsertDeleteServiceImpl implements LogInsertDeleteService {
    @Autowired
    private LogInsertDeleteDao logInsertDeleteDao;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmployeeDetailsDao employeeDetailsDao;
    private final JdbcTemplate jdbcTemplate;

    public LogInsertDeleteServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public ResponseEntity<?> getDetailsOfLog(LogDto logResult) {
        String sql="{call getLogInBatch(?,?,?,?)}";
        Object[]ob = new Object[4];
        if(logResult.getStart_date()==null && logResult.getEnd_date()==null)
        {
            ob[0]=logResult.getAction();
            ob[1]=logResult.getPage();
        }
        else if(logResult.getStart_date()!=null && logResult.getEnd_date()==null)
        {
            ob[0]=logResult.getAction();
            ob[1]=logResult.getPage();
            ob[2]=logResult.getStart_date();
        } else if (logResult.getStart_date()!=null && logResult.getEnd_date()!=null) {

            ob[0]=logResult.getAction();
            ob[1]=logResult.getPage();
            ob[2]=logResult.getStart_date();
            ob[3]=logResult.getEnd_date();
        }
        List<LogInsertDeleteDto> logInsertDeleteDtos  = jdbcTemplate.query(sql,ob,(resultSet,rowNum) ->{
            LogInsertDeleteDto logInsertDeleteDto = new LogInsertDeleteDto();
            logInsertDeleteDto .setLogId(resultSet.getLong("log_id"));
            logInsertDeleteDto .setAdminCode(resultSet.getString("admin_code"));
            logInsertDeleteDto .setEmpCode(resultSet.getString("emp_code"));
            logInsertDeleteDto .setEmpName(resultSet.getString("emp_name"));
            logInsertDeleteDto .setAdminName(resultSet.getString("admin_name"));
            logInsertDeleteDto.setAction(resultSet.getString("action"));
            logInsertDeleteDto .setTimeStamp(resultSet.getDate("time_stamp"));
            return logInsertDeleteDto ;
        });
        return (logInsertDeleteDtos.size() > 0) ? new ResponseEntity<>(logInsertDeleteDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data Found "), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getDetailsOfInsertDeleteLogById(String adminCode) {
        String sql="{call getInsertLogDetailsByAdminCode(?)}";
        List<LogInsertDeleteDto> logInsertDeleteDtos  = jdbcTemplate.query(sql,new Object[]{adminCode},(resultSet,rowNum) ->{
            LogInsertDeleteDto logInsertDeleteDto = new LogInsertDeleteDto();
            logInsertDeleteDto .setLogId(resultSet.getLong("log_id"));
            logInsertDeleteDto .setAdminCode(resultSet.getString("admin_code"));
            logInsertDeleteDto .setEmpCode(resultSet.getString("emp_code"));
            logInsertDeleteDto .setEmpName(resultSet.getString("emp_name"));
            logInsertDeleteDto .setAdminName(resultSet.getString("admin_name"));
            logInsertDeleteDto.setAction(resultSet.getString("action"));
            logInsertDeleteDto .setTimeStamp(resultSet.getDate("time_stamp"));
            return logInsertDeleteDto ;
        });
        return (logInsertDeleteDtos.size() > 0) ? new ResponseEntity<>(logInsertDeleteDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data Found "), HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> getInsertDeleteLogCount() {
        return new ResponseEntity<>(logInsertDeleteDao.count(), HttpStatus.OK);
    }

    public ResponseEntity<?> getDetailsOfInsertDeleteLogByempCode(String empCode) {
        String sql="{call getInsertLogDetailsByEmpCode(?)}";
        List<LogInsertDeleteDto> logInsertDeleteDtos  = jdbcTemplate.query(sql,new Object[]{empCode},(resultSet,rowNum) ->{
            LogInsertDeleteDto logInsertDeleteDto = new LogInsertDeleteDto();
            logInsertDeleteDto .setLogId(resultSet.getLong("log_id"));
            logInsertDeleteDto .setAdminCode(resultSet.getString("admin_code"));
            logInsertDeleteDto .setEmpCode(resultSet.getString("emp_code"));
            logInsertDeleteDto .setEmpName(resultSet.getString("emp_name"));
            logInsertDeleteDto .setAdminName(resultSet.getString("admin_name"));
            logInsertDeleteDto.setAction(resultSet.getString("action"));
            logInsertDeleteDto .setTimeStamp(resultSet.getDate("time_stamp"));
            return logInsertDeleteDto ;
        });
        return (logInsertDeleteDtos.size() > 0) ? new ResponseEntity<>(logInsertDeleteDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data Found "), HttpStatus.BAD_REQUEST);
    }

}