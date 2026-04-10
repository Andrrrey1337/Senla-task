package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Scooter Rental API",
                description = "Документация REST API для сервиса аренды самокатов",
                version = "1.0.0"
        )
)
public class OpenApiConfig {
}