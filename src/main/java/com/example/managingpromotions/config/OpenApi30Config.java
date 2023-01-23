package com.example.managingpromotions.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class OpenApi30Config {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = HttpHeaders.AUTHORIZATION;
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(
                        new Components()
                                .addSecuritySchemes("JWT",
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .scheme("Bearer")
                                                .bearerFormat("JWT")));
    }
}
