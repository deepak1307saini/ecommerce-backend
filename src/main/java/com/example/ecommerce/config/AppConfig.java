package com.example.ecommerce.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);

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
        logger.info("Creating RestTemplate bean");
        return new RestTemplate();
    }

    @Bean
    public Keycloak keycloak() {
        logger.info("Creating Keycloak admin client bean");
        return KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm(realm)
                .clientId(adminClientId)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret(adminClientSecret)
                .build();
    }
}