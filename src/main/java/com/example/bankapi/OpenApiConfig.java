package com.example.bankapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankApiOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("API Banque")
                .description("API de gestion des comptes bancaires")
                .version("v1")
            );
    }
}
