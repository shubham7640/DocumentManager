package com.springReact.DocumentManager.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotEmpty(message = "First name cannot be empty or null")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty or null")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty or null")
    @Email(message = "Invalid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty or null")
    private String password;

    private String phone;

    private String bio;
}
