package com.example.ecommerce.controller;

import com.example.ecommerce.dto.SignInRequest;
import com.example.ecommerce.dto.SignInResponse;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.enums.RoleEnum;
import com.example.ecommerce.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @ModelAttribute UserDTO userDTO,
                                            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        log.info("Processing registration request for user: {}", userDTO.getUsername());
        UserDTO user = userService.registerUser(userDTO, RoleEnum.USER.name());
        log.info("User registered: {}", user.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) {
        log.info("Processing sign-in request for user: {}", request.getUsername());
        return ResponseEntity.ok(userService.signIn(request));
    }

    @GetMapping("/sign-out")
    public ResponseEntity<Void> signOut() {
        log.info("Processing sign-out request");
        userService.signOut();
        return ResponseEntity.ok().build();
    }
}