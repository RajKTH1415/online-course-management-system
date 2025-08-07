package com.ocms.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI courseManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Online Course Management System")
                        .description("API documentation for managing online courses, users, enrollments, and reviews.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Rajkumar Prasad")
                                .email("rajkumarprasad@gmail.com")
                                .url("https://github.com/RajKTH1415"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project GitHub Repository")
                        .url("https://github.com/RajKTH1415/online-course-management-system"));
    }
}
