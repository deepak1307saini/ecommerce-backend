package com.example.ecommerce.service;

import com.example.ecommerce.dto.SignInRequest;
import com.example.ecommerce.dto.SignInResponse;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.User;

public interface UserService {
    UserDTO registerUser(UserDTO userDTO, String roleName);

    SignInResponse signIn(SignInRequest request);

    User getCurrentUser();

    User getUserIdByKeycloakId(String keycloakId);

    void signOut();
}
