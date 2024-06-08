package com.springReact.DocumentManager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springReact.DocumentManager.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class User {
    private Long id;
    private Long createdBy;
    private Long updatedBy;

    private String userId;

    private String firstName;

    private String lastName;
    private String email;
    private String phone;
    private String bio;

    private String imageUrl;
    private String qrCodeSecretUrl;
    private LocalDateTime lastLogin;
    private String createdAt;
    private String updatedAt;
    private String role;
    private String authorities;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private boolean mfa;

}
