package com.springReact.DocumentManager.service;

import com.springReact.DocumentManager.entity.RoleEntity;
import com.springReact.DocumentManager.enumeration.LoginType;

public interface UserService
{
    void createUser(String firstname,String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountToken(String token);
    void updateLoginAttempt(String email, LoginType loginType);
}
