package com.networkKnights.ecms.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class LogDto {

    String action;
    int page;
    Date start_date;
    Date end_date;
}