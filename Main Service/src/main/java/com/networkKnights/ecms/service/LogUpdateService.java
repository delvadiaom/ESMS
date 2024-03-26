package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dto.LogDto;
import org.springframework.http.ResponseEntity;

public interface LogUpdateService {
    public ResponseEntity<?>getDetailsOfLog(LogDto logResult);

    public ResponseEntity<?>getDetailsOfUpdateLogById(String adminCode);
    public ResponseEntity<?> getUpdateLogCount();
    public ResponseEntity<?> getDetailsOfUpdateLogByempCode(String empCode);
}