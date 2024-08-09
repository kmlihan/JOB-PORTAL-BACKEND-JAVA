package com.example.job_portal_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class OpenApiConfig {
    static {
        SpringDocUtils config = SpringDocUtils.getConfig();
        config
                .replaceWithSchema(LocalTime.class,
                        new Schema<LocalTime>()
                                .example(LocalTime.now().format(DateTimeFormatter.ISO_TIME)).type("string").format("time"))
                .replaceWithSchema(LocalDate.class,
                        new Schema<LocalDate>()
                                .example(LocalDate.now().format(DateTimeFormatter.ISO_DATE)).type("string").format("date"))
                .replaceWithSchema(LocalDateTime.class,
                        new Schema<LocalDateTime>()
                                .example(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).type("string"))
                .replaceWithSchema(ZonedDateTime.class,
                        new Schema<ZonedDateTime>()
                                .example(ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)).type("string"));
    }
    @Bean
    public OpenAPI customOpenAPI() {

        String securitySchemeName = "bearerAuth";
        OpenAPI openApi = new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8000"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info().title("Job portal API").description(
                        "Job portal API documentation in OpenAPI 3."));
        return openApi;
    }
}
