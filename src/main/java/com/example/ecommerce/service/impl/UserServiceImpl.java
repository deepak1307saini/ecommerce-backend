package com.example.ecommerce.service.impl;

import com.example.ecommerce.config.Context;
import com.example.ecommerce.dto.SignInRequest;
import com.example.ecommerce.dto.SignInResponse;
import com.example.ecommerce.dto.TenantDTO;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.RoleEnum;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.TenantRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.KeyClockUserService;
import com.example.ecommerce.service.TenantService;
import com.example.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final RestTemplate restTemplate;
    private final Keycloak keycloak;
    private final KeyClockUserService keyClockUserService;
    private final TenantService tenantService;

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Override
    public UserDTO registerUser(UserDTO userDTO, @NotBlank String roleName) {
        log.info("Registering user: {}", userDTO.getUsername());
//
        Response response = keyClockUserService.createUser(userDTO, roleName);

        String keycloakUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        log.debug("Created Keycloak user with ID: {}", keycloakUserId);

        // Save user in database
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setKeycloakUserId(keycloakUserId);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setMobileNumber(userDTO.getMobileNumber());

        Long tenantId = userDTO.getTenantId();
        if (nonNull(tenantId) && roleName.equals(RoleEnum.TENANT_ADMIN.toString())) {
            tenantRepository.findById(tenantId)
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));
            user.setTenantId(tenantId);
            log.debug("Assigned tenant ID {} to user {}", tenantId, userDTO.getUsername());
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO createUser(String username, String email, String roleName, Long tenantId) {
        log.info("Creating user: {}", username);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        if (nonNull(tenantId)) {
            tenantRepository.findById(tenantId)
                    .orElseThrow(() -> new RuntimeException("Tenant not found"));
            user.setTenantId(tenantId);
            log.debug("Assigned tenant ID {} to user {}", tenantId, username);
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());
        return toDTO(savedUser);
    }

    @Override
    public SignInResponse signIn(SignInRequest request) {
        log.info("Attempting sign-in for user: {}", request.getUsername());
        String url = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakServerUrl, realm);

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        // Create request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        // Send request
        AccessTokenResponse tokenResponse = restTemplate.postForObject(url, requestEntity, AccessTokenResponse.class);
        if (isNull(tokenResponse.getToken())) {
            log.error("Authentication failed for user: {}", request.getUsername());
            throw new RuntimeException("Authentication failed");
        }
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        TenantDTO tenantDTO = null;
        if (nonNull(user.getTenantId())) {
            tenantDTO=tenantService.getTenantById(user.getTenantId());
        }
        log.info("Sign-in successful for user: {}", request.getUsername());
        return SignInResponse.builder()
                .token(tokenResponse.getToken())
                .user(toDTO(user))
                .roles(roles)
                .tenant(tenantDTO)
                .build();
    }
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setTenantId(user.getTenantId());
        dto.setMobileNumber(user.getMobileNumber());
        return dto;
    }

    @Override
    public User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String keycloakId = jwt.getSubject();
        return userRepository.findByKeycloakUserId(keycloakId)
                .orElseThrow(() -> new IllegalStateException("User not found for keycloakId: " + keycloakId));
    }

    @Override
    public User getUserIdByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakUserId(keycloakId)
                .orElseGet(() -> {
                    log.warn("No user found for keycloakId: {}", keycloakId);
                    return null;
                });
    }

    @Override
    public void signOut() {
        String keycloakId = Context.getKeycloakId();
        try {
            UserResource userResource = keycloak.realm(realm).users().get(keycloakId);
            userResource.logout();
            log.info("Successfully logged out user with keycloakId: {}", keycloakId);
        } catch (Exception e) {
            log.error("Failed to logout user with keycloakId: {}", keycloakId, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to logout user", e);
        }
    }
}