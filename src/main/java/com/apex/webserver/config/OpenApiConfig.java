package com.apex.webserver.config;

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

@Configuration
public class OpenApiConfig {

    @Value("${springdoc.server.url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI apexOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Apex Finance API")
                        .description("API for managing financial transactions and accounting")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Apex Support")
                                .email("support@apex-finance.com"))
                        .license(new License()
                                .name("Private License")))
                .servers(List.of(
                        new Server().url(serverUrl).description("Server URL")
                ))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token with Bearer prefix, e.g. 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"));
    }
}