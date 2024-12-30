package com.akazaki.api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setName("Akazaki API Team");
        contact.setEmail("yannisalouache@gmail.com");

        Info info = new Info()
            .title("Akazaki API Documentation")
            .version("1.0")
            .contact(contact)
            .description("This API exposes endpoints for Akazaki e-commerce system.");

        return new OpenAPI()
            .info(info)
            .servers(List.of(devServer));
    }
} 