package com.springReact.DocumentManager.service;

import com.springReact.DocumentManager.dto.User;
import com.springReact.DocumentManager.entity.CredentialEntity;
import com.springReact.DocumentManager.entity.RoleEntity;
import com.springReact.DocumentManager.enumeration.LoginType;

public interface UserService
{
    void createUser(String firstname,String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountToken(String token);
    void updateLoginAttempt(String email, LoginType loginType);

    User getUserByUserId(String userId);

    User getUserByEmail(String email);

    CredentialEntity getUserCredentialsById(Long id);
}
