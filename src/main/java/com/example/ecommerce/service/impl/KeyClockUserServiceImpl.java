package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.service.KeyClockUserService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeyClockUserServiceImpl implements KeyClockUserService {
    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public Response createUser(UserDTO userDTO, String roleName) {
        try {
            UserRepresentation user = getUserRepresentation(userDTO);

            // Create user in Keycloak
            UsersResource usersResource = keycloak.realm(realm).users();
            Response response = usersResource.create(user);

            if (response.getStatus() != 201) {
                log.error("Failed to create user in Keycloak: Status {}, Info: {}",
                        response.getStatus(), response.getStatusInfo());
                throw new RuntimeException("Failed to create user in Keycloak: " + response.getStatusInfo());
            }

            // Extract user ID
            String locationHeader = (String) response.getHeaders().getFirst("Location");
            String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
            log.info("User created with ID: {} and username: {}", userId, userDTO.getUsername());

            // Assign role explicitly
            assignRole(userId, roleName);

            // Verify role assignment
            UserResource userResource = usersResource.get(userId);
            List<RoleRepresentation> assignedRoles = userResource.roles().realmLevel().listEffective();
            boolean roleAssigned = assignedRoles.stream()
                    .anyMatch(role -> roleName.equals(role.getName()));
            if (roleAssigned) {
                log.info("Role {} successfully assigned to user {}", roleName, userDTO.getUsername());
            } else {
                log.error("Failed to assign role {} to user {}", roleName, userDTO.getUsername());
                throw new RuntimeException("Failed to assign role " + roleName + " to user");
            }

            return response;
        } catch (Exception e) {
            log.error("Error creating user in Keycloak: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating user in Keycloak", e);
        }
    }

    private static UserRepresentation getUserRepresentation(UserDTO userDTO) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmailVerified(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(userDTO.getPassword());
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        user.setCredentials(Collections.singletonList(credentialRepresentation));
        return user;
    }

    private UsersResource getUserResource() {
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    @Override
    public UserRepresentation getUserById(String userId) {
        return getUserResource().get(userId).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId) {
        getUserResource().delete(userId);
    }

    private UserResource getUserResource(String userId) {
        return getUserResource().get(userId);
    }

    @Override
    public void assignRole(String userId, String roleName) {
        try {
            UserResource userResource = getUserResource(userId);
            RolesResource rolesResource = getRolesResource();
            RoleRepresentation representation;
            try {
                representation = rolesResource.get(roleName).toRepresentation();
                log.info("Assigning role {} (ID: {}) to user ID {}", roleName, representation.getId(), userId);
            } catch (Exception e) {
                log.error("Role {} not found in realm {}", roleName, realm, e);
                throw new RuntimeException("Role " + roleName + " not found in Keycloak");
            }
            userResource.roles().realmLevel().add(Collections.singletonList(representation));
            log.info("Role {} successfully assigned to user ID {}", roleName, userId);
        } catch (Exception e) {
            log.error("Error assigning role {} to user ID {}: {}", roleName, userId, e.getMessage(), e);
            throw new RuntimeException("Error assigning role " + roleName + " to user", e);
        }
    }

    private RolesResource getRolesResource() {
        return keycloak.realm(realm).roles();
    }
}