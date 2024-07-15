package com.ucan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.ucan.dao")
public class UcanAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(UcanAdminApplication.class, args);
    }
}
