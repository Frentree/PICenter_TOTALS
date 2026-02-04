package com.org.iopts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * PICenter_RM Spring Boot Application
 *
 * Spring Boot 3.2.5 기반 개인정보 탐지 관리 시스템
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.org.iopts")
public class PicenterRmApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicenterRmApplication.class, args);
    }
}
