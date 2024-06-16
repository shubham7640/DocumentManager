package com.springReact.DocumentManager.util;


import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.springReact.DocumentManager.constants.Constants;
import com.springReact.DocumentManager.dto.User;
import com.springReact.DocumentManager.entity.CredentialEntity;
import com.springReact.DocumentManager.entity.RoleEntity;
import com.springReact.DocumentManager.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.springReact.DocumentManager.constants.Constants.EMPTY_STRING;

public class UserUtil {
    public static UserEntity createuserEntity(String firstName, String lastName, String email, RoleEntity role)
    {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .role(role)
                .lastLogin(LocalDateTime.now())
                .accountNonExpired(false)
                .accountNonLocked(false)
                .enabled(false)
                .mfa(false)
                .loginAttempts(0)
                .qrCodeSecret(EMPTY_STRING) //TODO: they have imported apche commons dependency but not required at the moment
                .phone(EMPTY_STRING)
                .bio(EMPTY_STRING)
                .imageUrl("https://freepngimg.com/thumb/car/1-2-car-png-picture.png")
                .build();
    }
    public static User fromUserEntity(UserEntity userEntity, RoleEntity role, CredentialEntity credentialEntity) {
        User user = new User();
        BeanUtils.copyProperties(userEntity,user);
        user.setLastLogin(userEntity.getLastLogin());
        user.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(role.getRoleName());
        user.setAuthorities(role.getAuthorites().getValue());
        return user;
    }

    private static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(Constants.NINETY_DAYS).isAfter(LocalDateTime.now());
    }
}
