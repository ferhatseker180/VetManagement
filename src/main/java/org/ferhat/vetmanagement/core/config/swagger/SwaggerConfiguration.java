package org.ferhat.vetmanagement.core.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Vet Management System")
                .description("Vet Management System REST API")
                .contact(new Contact().name("Ferhat"))
                .version("1.0.0"));
    }

}
