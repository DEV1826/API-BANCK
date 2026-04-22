package com.example.bankapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bankApiOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:3000/api-docs/");
        devServer.setDescription("Serveur de développement");

        Server prodServer = new Server();
        prodServer.setUrl("https://api-banck-nglc.onrender.com");
        prodServer.setDescription("Serveur de production");

        Contact contact = new Contact();
        contact.setName("Japhet Merime Djomo Yondjio");
        contact.setEmail("japhetdjomo@gmail.com");

        Info info = new Info()
            .title("API Bancaire - Système de transactions")
            .description("API complète pour la création de comptes, dépôts, retraits, consultation, liste paginée et historique des transactions.")
            .version("1.0.0")
            .contact(contact);

        return new OpenAPI()
            .components(new Components())
            .info(info)
            .servers(List.of(devServer, prodServer));
    }
}
