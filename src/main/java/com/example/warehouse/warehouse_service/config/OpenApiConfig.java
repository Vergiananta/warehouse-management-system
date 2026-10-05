package com.example.warehouse.warehouse_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI warehouseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Warehouse Management Service API")
                        .description("REST API documentation for Warehouse Management System handling Items, Item Variants, and Orders")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Warehouse Service Team")
                                .email("christian.vergi@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")));
    }
}
