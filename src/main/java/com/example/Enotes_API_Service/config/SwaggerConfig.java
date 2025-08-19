package com.example.Enotes_API_Service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        Info info = new Info();
        info.setTitle("E-notes API");
        info.setDescription("API documentation for E-notes application");
        info.setVersion("1.0.0");

        List<Server> serverList = List.of(new Server().description("Dev").url("http://localhost:9090"),
        new Server().description("Test").url("http://localhost:9091"),
        new Server().description("Prod").url("http://localhost:9092"),
        new Server().description("Uat").url("http://localhost:9093"));

        SecurityScheme securityScheme = new SecurityScheme().name("Authorization")
                        .scheme("bearer").type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT").in(SecurityScheme.In.HEADER);

        Components components = new Components().addSecuritySchemes("Token", securityScheme);

        openAPI.setServers(serverList);
        openAPI.setInfo(info);
        openAPI.setComponents(components);
        openAPI.setSecurity(List.of(new SecurityRequirement().addList("Token")));

        return openAPI;
    }

}
