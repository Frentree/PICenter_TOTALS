package com.org.iopts.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) Configuration
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // API Info
                .info(new Info()
                        .title("PICenter TOTALS API")
                        .description("Legacy Spring MVC → Spring Boot REST API Migration")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("PICenter Team")
                                .email("support@picenter.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))

                // Servers
                .servers(List.of(
                        new Server()
                                .url("http://192.168.2.241:" + serverPort)
                                .description("External Server"),
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Server")
                ))

                // Security Scheme (JWT Bearer Token)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Authentication Token")
                        ))

                // Global Security Requirement
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName));
    }
}
