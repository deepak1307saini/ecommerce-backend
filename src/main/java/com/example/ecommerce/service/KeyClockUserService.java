package com.example.ecommerce.service;

import com.example.ecommerce.dto.UserDTO;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeyClockUserService {
    Response createUser(UserDTO userRegistrationRecord, String roleName);
    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);

    void assignRole(String userId, String roleName);
}
