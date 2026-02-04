package com.org.iopts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI picenterOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PICenter_RM API")
                        .description("Personal Information Center Resource Management API")
                        .version("rm_2.0")
                        .contact(new Contact()
                                .name("PICenter Team")
                                .email("admin@picenter.com")))
                .servers(List.of(
                        new Server().url("http://192.168.2.241:8080/PICenter_RM").description("Development Server"),
                        new Server().url("http://localhost:8080/PICenter_RM").description("Local Server")
                ));
    }
}
