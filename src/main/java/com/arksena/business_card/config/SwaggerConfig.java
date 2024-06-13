package com.arksena.business_card.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Business Card API")
                        .version("1.0.0")
                        .description("API for managing business cards and user registrations.")
                )
                .components(new Components()
                        .addSecuritySchemes("basicAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                        )
                        .addSchemas("CardDTO", new Schema<>()
                                .type("object")
                                .addProperty("id", new Schema<>()
                                        .type("integer")
                                        .format("int64")
                                )
                                .addProperty("user", new Schema<>()
                                        .$ref("#/components/schemas/UserDTO")
                                )
                                .addProperty("title", new Schema<>()
                                        .type("string")
                                )
                                .addProperty("description", new Schema<>()
                                        .type("string")
                                )
                                .addProperty("contactInfo", new Schema<>()
                                        .type("string")
                                )
                                .addProperty("cardType", new Schema<>()
                                        .type("string")
                                )
                                .addProperty("data", new Schema<>()
                                        .type("string")
                                )
                        )
                        .addSchemas("UserDTO", new Schema<>()
                                .type("object")
                                .addProperty("id", new Schema<>()
                                        .type("integer")
                                        .format("int64")
                                )
                                .addProperty("username", new Schema<>()
                                        .type("string")
                                )
                        )
                        .addSchemas("User", new Schema<>()
                                .type("object")
                                .addProperty("id", new Schema<>()
                                        .type("integer")
                                        .format("int64")
                                )
                                .addProperty("username", new Schema<>()
                                        .type("string")
                                )
                                .addProperty("email", new Schema<>()
                                        .type("string")
                                        .format("email")
                                )
                                .addProperty("passwordHash", new Schema<>()
                                        .type("string")
                                )
                                .addProperty("role", new Schema<>()
                                        .type("string")
                                )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("basicAuth", Arrays.asList()))
                .addServersItem(new Server().url("http://localhost:8080/"))
                .addServersItem(new Server().url("http://localhost:2345/"));
    }
}