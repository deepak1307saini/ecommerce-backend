package com.example.ecommerce.dto;

import com.example.ecommerce.util.Constants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TenantAdminRequest {
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

    @NotBlank(message = "TenantId is required")
    private Long tenantId;

    @Pattern(regexp = Constants.PHONE_NUMBER_REGEX, message = "Invalid mobile number format")
    private String mobileNumber;

    private String profilePicturePath;
}