package com.org.iopts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * PICenter_TOTALS Main Application
 *
 * Legacy Spring MVC → Spring Boot REST API Migration
 *
 * @author PICenter Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class PicenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicenterApplication.class, args);
    }
}
