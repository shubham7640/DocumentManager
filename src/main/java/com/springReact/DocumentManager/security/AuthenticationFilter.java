package com.springReact.DocumentManager.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springReact.DocumentManager.domain.ApiAuthentication;
import com.springReact.DocumentManager.dtorequest.LoginRequest;
import com.springReact.DocumentManager.enumeration.LoginType;
import com.springReact.DocumentManager.service.JwtService;
import com.springReact.DocumentManager.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import com.springReact.DocumentManager.util.RequestUtil;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final UserService userService;

    private final JwtService jwtService;

    protected AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,JwtService jwtService) {
        super(new AntPathRequestMatcher("/user/login", HttpMethod.POST.name()),authenticationManager);
        this.jwtService=jwtService;
        this.userService=userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        //Please note we should not use try catch or handle any exception in this method as
        // exceptions are expected to be traversed to built in method unsuccessfulAuthentication
        try{
            var user = new ObjectMapper().configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true).readValue(request.getInputStream(), LoginRequest.class);

            //Please note that email should be valid in order to count it as a login attempt
            userService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_ATTEMPT);
            var authentication = ApiAuthentication.unauthenticated(user.getEmail(), user.getPassword());
            return getAuthenticationManager().authenticate(authentication);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
            RequestUtil.handleErrorResponse(request,response,e);
            return null;
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}





