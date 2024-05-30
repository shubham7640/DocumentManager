package com.springReact.DocumentManager.service;

import com.springReact.DocumentManager.entity.RoleEntity;

public interface UserService
{
    void createUser(String firstname,String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountToken(String token);
}
