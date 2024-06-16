package com.springReact.DocumentManager.security;

import com.springReact.DocumentManager.constants.Constants;
import com.springReact.DocumentManager.domain.ApiAuthentication;
import com.springReact.DocumentManager.domain.UserPrincipal;
import com.springReact.DocumentManager.exception.ApiException;
import com.springReact.DocumentManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {


    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


//    private final UserDetailsService userDetailsService;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthentication = authenticationFunction.apply(authentication);
        var user = userService.getUserByEmail(apiAuthentication.getEmail());
        if(user!=null)
        {
            var userCredential = userService.getUserCredentialsById(user.getId());
            if(userCredential.getUpdatedAt().minusDays(Constants.NINETY_DAYS).isAfter(LocalDateTime.now()))
            {
                throw new ApiException("Credentials are expired. Please reset your password");
            }
            var userPrincipal = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrincipal);

            if(bCryptPasswordEncoder.matches(apiAuthentication.getPassword(),userCredential.getPassword())){
                return ApiAuthentication.authenticated(user,userPrincipal.getAuthorities());
            }
            else {
                throw new BadCredentialsException("Email or Password incorrect. Please try again");
            }
        }
        else
            throw new ApiException("Unable to authenticate");
    }
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        var user = (UsernamePasswordAuthenticationToken) authentication;
//        var userFromDB = userDetailsService.loadUserByUsername((String) user.getPrincipal());
//        var password = (String) user.getCredentials();
//
//        if(password.equals(userFromDB.getPassword()))
//        {
//            return UsernamePasswordAuthenticationToken.authenticated(userFromDB,"[PASSWORD PROTECTED]",userFromDB.getAuthorities());
//        }
//        throw new BadCredentialsException("Unable to authenticate user");
//    }

    @Override
    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(userPrincipal.isAccountNonLocked()){
            throw new LockedException("Account is currently locked");
        }
        if(userPrincipal.isEnabled()){
            throw new DisabledException("Account is currently disabled");
        }
        if(userPrincipal.isCredentialsNonExpired()){
            throw new CredentialsExpiredException("Password has expired. Please reset account password");
        }
        if(userPrincipal.isAccountNonExpired()){
            throw new DisabledException("Account has expired. Please reset account password or contact admin desk");
        }
    };
}
