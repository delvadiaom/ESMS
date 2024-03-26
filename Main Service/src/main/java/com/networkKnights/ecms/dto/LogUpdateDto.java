package com.networkKnights.ecms.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class LogUpdateDto {

    long logId;

    String adminCode;

    String empCode;
    String empName;
    String adminName;

    String columnName;

    String oldValue;

    String newValue;

    Date timeStamp;

}



