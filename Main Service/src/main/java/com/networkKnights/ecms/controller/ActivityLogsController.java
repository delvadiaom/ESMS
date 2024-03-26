package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.dto.LogDto;
import com.networkKnights.ecms.util.ESMSError;
import com.networkKnights.ecms.service.LogInsertDeleteService;
import com.networkKnights.ecms.service.LogUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/activityLogs")
public class ActivityLogsController {
    @Autowired
    private LogUpdateService logUpdateService;
    @Autowired
    LogInsertDeleteService logInsertDeleteService;

    @GetMapping("/updateLogCount")
    public ResponseEntity<?> getUpdateLogCount()
    {
        return logUpdateService.getUpdateLogCount();
    }

    @GetMapping("/insertDeleteLogCount")
    public ResponseEntity<?> getInsertDeleteLogCount() {
        return logInsertDeleteService.getInsertDeleteLogCount();
    }

    @PostMapping("/getDetailsOfLog")
    public ResponseEntity<?> getDetailsOfLog(@RequestBody LogDto logResult) {

        if (logResult.getAction().equals("update")) {
            return logUpdateService.getDetailsOfLog(logResult);
        } else {

            return logInsertDeleteService.getDetailsOfLog(logResult);
        }
    }
    @GetMapping("/getDetailsOfUpdateLogByAdminCode/{adminCode}")
    public ResponseEntity<?>getDetailsOfUpdateLogById(@PathVariable String adminCode)
    {
        return logUpdateService.getDetailsOfUpdateLogById(adminCode);
    }

    @GetMapping("/getDetailsOfInsertDeleteLogByAdminCode/{adminCode}")
    public ResponseEntity<?>getDetailsOfInsertDeleteLogById(@PathVariable String adminCode)
    {
        return logInsertDeleteService.getDetailsOfInsertDeleteLogById(adminCode);
    }
    @GetMapping("/getDetailsOfUpdateLogByEmpCode/{empCode}")
    public ResponseEntity<?>getDetailsOfUpdateLogByEmpCode(@PathVariable String empCode)
    {
        return logUpdateService.getDetailsOfUpdateLogByempCode(empCode);
    }

    @GetMapping("/getDetailsOfInsertDeleteLogByEmpCode/{empCode}")
    public ResponseEntity<?>getDetailsOfInsertDeleteLogByEmpCode(@PathVariable String empCode)
    {
        return logInsertDeleteService.getDetailsOfInsertDeleteLogByempCode(empCode);
    }
}
