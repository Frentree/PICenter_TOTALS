package com.org.iopts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) Configuration
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI picenterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PICenter RM API")
                        .description("개인정보 탐지 관리 시스템 REST API (Spring Boot 3.2.5)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("IOPTS Team")
                                .email("admin@iopts.com")))
                .servers(List.of(
                        new Server().url("http://192.168.2.241:8090").description("Development Server (8090)"),
                        new Server().url("http://localhost:8090").description("Local Server (8090)")
                ));
    }
}
