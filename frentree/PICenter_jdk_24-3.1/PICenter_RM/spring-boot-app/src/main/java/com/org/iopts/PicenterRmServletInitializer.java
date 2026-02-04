package com.org.iopts;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Spring Boot Servlet Initializer
 * 외부 Tomcat 배포를 위한 설정
 */
public class PicenterRmServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PicenterRmApplication.class);
    }
}
