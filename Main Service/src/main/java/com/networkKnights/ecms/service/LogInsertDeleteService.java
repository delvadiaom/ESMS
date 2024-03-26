package com.networkKnights.ecms.service;


import com.networkKnights.ecms.dto.LogDto;

import org.springframework.http.ResponseEntity;


public interface LogInsertDeleteService {

    public ResponseEntity<?> getDetailsOfLog(LogDto logResult);

    public ResponseEntity<?>getDetailsOfInsertDeleteLogById(String adminCode);

    public ResponseEntity<?> getInsertDeleteLogCount();

    public ResponseEntity<?> getDetailsOfInsertDeleteLogByempCode(String empCode);
}
