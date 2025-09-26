package com.example.ecommerce.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class AppConfig {

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-client-secret}")
    private String adminClientSecret;

    @Bean
    public RestTemplate restTemplate() {
        log.info("Creating RestTemplate bean");
        return new RestTemplate();
    }

    @Bean
    public Keycloak keycloak() {
        log.info("Creating Keycloak admin client bean");
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm(realm)
                .clientId(adminClientId)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret(adminClientSecret)
                .build();
    }
}