package com.networkKnights.ecms.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class CostingOfDeptDto {
    private String departmentName;
    private HashMap<String,String> costing;
}
