package com.springReact.DocumentManager.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springReact.DocumentManager.constants.Constants;
import com.springReact.DocumentManager.domain.ApiAuthentication;
import com.springReact.DocumentManager.domain.Response;
import com.springReact.DocumentManager.dto.User;
import com.springReact.DocumentManager.dtorequest.LoginRequest;
import com.springReact.DocumentManager.enumeration.LoginType;
import com.springReact.DocumentManager.enumeration.TokenType;
import com.springReact.DocumentManager.service.JwtService;
import com.springReact.DocumentManager.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;

import com.springReact.DocumentManager.util.RequestUtil;

@Slf4j
public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private final UserService userService;

    private final JwtService jwtService;

    protected AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,JwtService jwtService) {
        super(new AntPathRequestMatcher(Constants.LOGIN_PATH, HttpMethod.POST.name()),authenticationManager);
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
        var user = (User)authResult.getPrincipal();
        userService.updateLoginAttempt(user.getEmail(), LoginType.LOGIN_SUCCESS);
        var httpResponse = user.isMfa()? sendQrCode(request,user) : sendresponse(request,response,user);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        var out = response.getOutputStream();
        var mapper = new ObjectMapper();
        mapper.writeValue(out,httpResponse);
        out.flush();
//        super.successfulAuthentication(request, response, chain, authResult);
    }

    private Response sendresponse(HttpServletRequest request, HttpServletResponse response, User user) {
        jwtService.addCookie(response,user, TokenType.ACCESS);
        jwtService.addCookie(response,user, TokenType.REFRESH);
        return RequestUtil.getResponse(request, Map.of("user",user),"Login Success", HttpStatus.OK);

    }

    private Response sendQrCode(HttpServletRequest request, User user) {

        return RequestUtil.getResponse(request,Map.of("user",user),"Please enter QR code",HttpStatus.OK);
    }
}





