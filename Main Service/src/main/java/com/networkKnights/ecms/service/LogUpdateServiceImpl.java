package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.EmployeeDetailsDao;
import com.networkKnights.ecms.dao.LogUpdateDao;
import com.networkKnights.ecms.dto.LogDto;
import com.networkKnights.ecms.dto.LogUpdateDto;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.util.EmployeeDetailsConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.List;


@Service
public class LogUpdateServiceImpl implements LogUpdateService {

    @Autowired

    private LogUpdateDao logUpdateDao;
    @Autowired
    private EmployeeDetailsDao employeeDetailsDao;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private ModelMapper modelMapper;
    private final JdbcTemplate jdbcTemplate;

    public LogUpdateServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Cacheable(value = "logUpdates")

    public ResponseEntity<?> getDetailsOfLog(LogDto logResult) {

        String sql = "{call getLogInBatch(?,?,?,?)}";
        Object[] ob = new Object[4];
        if (logResult.getStart_date() == null && logResult.getEnd_date() == null) {
            ob[0] = logResult.getAction();
            ob[1] = logResult.getPage();
        } else if (logResult.getStart_date() != null && logResult.getEnd_date() == null) {
            ob[0] = logResult.getAction();
            ob[1] = logResult.getPage();
            ob[2] = logResult.getStart_date();
        } else if (logResult.getStart_date() != null && logResult.getEnd_date() != null) {

            ob[0] = logResult.getAction();
            ob[1] = logResult.getPage();
            ob[2] = logResult.getStart_date();
            ob[3] = logResult.getEnd_date();
        }
        List<LogUpdateDto> logUpdateDtos = jdbcTemplate.query(sql, ob, (resultSet, rowNum) -> {
            LogUpdateDto logUpdateDto = new LogUpdateDto();
            logUpdateDto.setLogId(resultSet.getLong("log_id"));
            logUpdateDto.setAdminCode(resultSet.getString("admin_code"));
            logUpdateDto.setEmpCode(resultSet.getString("emp_code"));
            logUpdateDto.setEmpName(resultSet.getString("emp_name"));
            logUpdateDto.setAdminName(resultSet.getString("admin_name"));
            logUpdateDto.setColumnName(EmployeeDetailsConstant.arr.get((int) resultSet.getLong("column_id")));
            if(resultSet.getLong("column_id") > 20 && resultSet.getLong("column_id") < 28){
                logUpdateDto.setOldValue(encryptionService.decrypt(resultSet.getString("old_value")));
                logUpdateDto.setNewValue(encryptionService.decrypt(resultSet.getString("new_value")));
            }else{
                logUpdateDto.setOldValue(resultSet.getString("old_value"));
                logUpdateDto.setNewValue(resultSet.getString("new_value"));
            }
            logUpdateDto.setTimeStamp(resultSet.getDate("time_stamp"));
            return logUpdateDto;
        });
        return (logUpdateDtos.size() > 0) ? new ResponseEntity<>(logUpdateDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data Found "), HttpStatus.BAD_REQUEST);
    }


    @Override

    public ResponseEntity<?> getDetailsOfUpdateLogById(String adminCode) {

        String sql = "{call getupdateLogDetailsByAdminCode(?)}";
        List<LogUpdateDto> logUpdateDtos = jdbcTemplate.query(sql,new Object[]{adminCode}, (resultSet, rowNum) -> {
            LogUpdateDto logUpdateDto = new LogUpdateDto();
            logUpdateDto.setLogId(resultSet.getLong("log_id"));
            logUpdateDto.setAdminCode(resultSet.getString("admin_code"));
            logUpdateDto.setEmpCode(resultSet.getString("emp_code"));
            logUpdateDto.setEmpName(resultSet.getString("emp_name"));
            logUpdateDto.setAdminName(resultSet.getString("admin_name"));
            logUpdateDto.setColumnName(EmployeeDetailsConstant.arr.get((int) resultSet.getLong("column_id")));
            if(resultSet.getLong("column_id") > 20 && resultSet.getLong("column_id") < 28){
                logUpdateDto.setOldValue(encryptionService.decrypt(resultSet.getString("old_value")));
                logUpdateDto.setNewValue(encryptionService.decrypt(resultSet.getString("new_value")));
            }else{
                logUpdateDto.setOldValue(resultSet.getString("old_value"));
                logUpdateDto.setNewValue(resultSet.getString("new_value"));
            }
            logUpdateDto.setTimeStamp(resultSet.getDate("time_stamp"));
            return logUpdateDto;
        });
        return (logUpdateDtos.size() > 0) ? new ResponseEntity<>(logUpdateDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data Found "), HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<?> getUpdateLogCount() {
        return new ResponseEntity<>(logUpdateDao.count(), HttpStatus.OK);
    }

    public ResponseEntity<?> getDetailsOfUpdateLogByempCode(String empCode) {
        String sql = "{call getupdateLogDetailsByEmpCode(?)}";
        List<LogUpdateDto> logUpdateDtos = jdbcTemplate.query(sql,new Object[]{empCode}, (resultSet, rowNum) -> {
            LogUpdateDto logUpdateDto = new LogUpdateDto();
            logUpdateDto.setLogId(resultSet.getLong("log_id"));
            logUpdateDto.setAdminCode(resultSet.getString("admin_code"));
            logUpdateDto.setEmpCode(resultSet.getString("emp_code"));
            logUpdateDto.setEmpName(resultSet.getString("emp_name"));
            logUpdateDto.setAdminName(resultSet.getString("admin_name"));
            logUpdateDto.setColumnName(EmployeeDetailsConstant.arr.get((int) resultSet.getLong("column_id")));
            if(resultSet.getLong("column_id") > 20 && resultSet.getLong("column_id") < 28){
                logUpdateDto.setOldValue(encryptionService.decrypt(resultSet.getString("old_value")));
                logUpdateDto.setNewValue(encryptionService.decrypt(resultSet.getString("new_value")));
            }else{
                logUpdateDto.setOldValue(resultSet.getString("old_value"));
                logUpdateDto.setNewValue(resultSet.getString("new_value"));
            }
            logUpdateDto.setTimeStamp(resultSet.getDate("time_stamp"));
            return logUpdateDto;
        });
        return (logUpdateDtos.size() > 0) ? new ResponseEntity<>(logUpdateDtos, HttpStatus.OK) : new ResponseEntity<>(new ESMSError("No Data Found "), HttpStatus.BAD_REQUEST);
    }


}
