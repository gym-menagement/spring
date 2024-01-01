package com.domain.gym.gymspring.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(info = @Info(title = "GYM API 명세서", description = "GYM 서비스 API 명세서", version = "v0.0.1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        String[] paths = { "/api/**" };

        return GroupedOpenApi.builder()
                .group("gym 서비스 API v1")
                .pathsToMatch(paths)
                .build();
    }
}