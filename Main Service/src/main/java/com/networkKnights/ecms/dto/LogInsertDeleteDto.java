package com.networkKnights.ecms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class LogInsertDeleteDto {
    long logId;
    String adminCode;
    String adminName;
    String empName;
    String empCode;
    String action;
    Date timeStamp;
}