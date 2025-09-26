package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.dto.UserRequest;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @PreAuthorize("hasRole('PLATFORM_ADMIN') or hasRole('TENANT_ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequest request) {
        log.info("Creating user: {}", request.getUsername());
        UserDTO user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                "USR",
                null
        );
        log.info("User created: {}", request.getUsername());
        return ResponseEntity.ok(user);
    }
}