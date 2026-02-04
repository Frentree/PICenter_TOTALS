package com.org.iopts.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringDocWebMvcConfiguration.class)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("PICenter_RM")
                .pathsToMatch("/**")
                .build();
    }
}
