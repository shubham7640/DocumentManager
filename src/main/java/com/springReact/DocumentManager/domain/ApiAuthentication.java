package com.springReact.DocumentManager.domain;

import com.springReact.DocumentManager.dto.User;
import com.springReact.DocumentManager.exception.ApiException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class ApiAuthentication  extends AbstractAuthenticationToken {
//  Please note UsernamePasswordAuthenticationToken also extends to AbstractAuthenticationToken
    private static final String PASSWORD_PROTECTED = "[PASSWORD_PROTECTED]";
    private static final String EMAIL_PROTECTED = "[EMAIL_PROTECTED]";

    private User user;
    private String email;
    private String password;
    private boolean authenticated;

    //Contructors are private so that objects are not created outside the class
    private ApiAuthentication(String email,String password) {
        super(AuthorityUtils.NO_AUTHORITIES);

        this.email = email;
        this.password = password;
        this.authenticated = false;
    }
    private ApiAuthentication(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.email = EMAIL_PROTECTED;
        this.password = PASSWORD_PROTECTED;
        // user is already authenticated hence no need to show email and pwd
        this.authenticated = true;
    }
    public static ApiAuthentication unauthenticated(String email,String password) {
       return new ApiAuthentication(email,password);
    }
    public static ApiAuthentication authenticated(User user, Collection<? extends GrantedAuthority> authorities) {
        return new ApiAuthentication(user,authorities);
    }

    @Override
    public Object getCredentials() {
        return PASSWORD_PROTECTED;
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        throw new ApiException("You are not allowed to set the authenticated token");
        //Direct Authentication setting is not allowed
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getEmail()
    {
        return this.email;
    }
}
