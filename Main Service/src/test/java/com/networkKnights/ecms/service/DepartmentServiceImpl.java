package com.networkKnights.ecms.service;

import com.networkKnights.ecms.dao.DepartmentDao;
import com.networkKnights.ecms.dto.DepartmentDto;
import com.networkKnights.ecms.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class DepartmentServiceImplTest {

    @Autowired
    private DepartmentService departmentService;
    @MockBean
    private DepartmentDao departmentDao;
    @BeforeEach
    void setUp(){
        Optional<Department> department = Optional.of(new Department(46,"java 12","dept01","java, jeee,html","emp02","2023:8258000###2022:56985###2021:456987",new Date(),new Date(),true));
        Mockito.when(departmentDao.findById(46L)).thenReturn(department);
    }

    @Test
    public void getDepartmentDetails() throws Exception {

        long departmentId = 46;
        ResponseEntity<?> department = departmentService.getDepartmentDetails(46);
        DepartmentDto department1 = (DepartmentDto) department.getBody();
        assertEquals(departmentId, department1.getDepartmentId());

    }


}