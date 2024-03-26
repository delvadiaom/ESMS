package com.networkKnights.ecms.controller;

import com.networkKnights.ecms.constant.URLConstant;
import com.networkKnights.ecms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping(URLConstant.DASHBOARD)
public class DashBoardController {
    @Autowired
    private DashboardService dashboardService;
    @GetMapping(URLConstant.GET)
    public ResponseEntity<?> getDashboardDetails() {
        return dashboardService.getDashboardDetails();
    }

    @Bean
    public WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("*");
            }
        };
    }
}
