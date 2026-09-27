package com.example.DealerFlow.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI dealerFlowOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("DealerFlow API")
                        .version("1.0.0")
                        .description("API REST para autenticação, consultas e análises de concessionárias e modelos de veículos. "
                                + "As operações protegidas exigem um token JWT no formato Bearer. "
                                + "O esquema de segurança está disponível para uso nas operações protegidas; "
                                + "login e criação de usuário são públicos.")
                        .contact(new Contact()
                                .name("Equipe DealerFlow")))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Informe o token JWT obtido em POST /auth/login. Não inclua a palavra Bearer neste campo.")));
    }
}