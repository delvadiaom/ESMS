package com.networkKnights.ecms.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api()
    {
        return  new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo())
                .select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
    }
    private ApiInfo getInfo()
    {
        return new ApiInfo("Employee Salary Management System",null,"2.0",null,new Contact("name",null,"ujefmalek49@gamil.com"),null,null, Collections.emptyList());
    }
}