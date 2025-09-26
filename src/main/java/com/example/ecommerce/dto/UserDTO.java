package com.example.ecommerce.dto;

import com.example.ecommerce.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String password;

    private Long tenantId;
    private String tenantName;

    @Pattern(regexp = Constants.PHONE_NUMBER_REGEX, message = "Invalid mobile number format")
    private String mobileNumber;

    private String profilePicturePath; // Optional field for storing file path

    private List<String> roles;

}