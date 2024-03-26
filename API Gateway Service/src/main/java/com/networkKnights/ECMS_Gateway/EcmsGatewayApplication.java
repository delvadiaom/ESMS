package com.networkKnights.ECMS_Gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableEurekaClient
@CrossOrigin(origins = {"*"})
public class EcmsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcmsGatewayApplication.class, args);
    }

}
