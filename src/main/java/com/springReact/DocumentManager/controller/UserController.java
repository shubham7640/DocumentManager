package com.springReact.DocumentManager.controller;

import com.springReact.DocumentManager.domain.Response;
import com.springReact.DocumentManager.dtorequest.UserRequest;
import com.springReact.DocumentManager.service.UserService;

import com.springReact.DocumentManager.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

import static java.util.Collections.emptyMap;


@RestController
@RequiredArgsConstructor //No need of autowired after this as we have already initialized the params
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

//  Authentication has been moved to project's filter class
//    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest userRequest, HttpServletRequest httpServletRequest)
    {
        userService.createUser(userRequest.getFirstName(),userRequest.getLastName(),userRequest.getEmail(),userRequest.getPassword());
        return ResponseEntity.created(getUri()).body(RequestUtil.getResponse(httpServletRequest,emptyMap(),"Account Created. Please check your email to enable your account", HttpStatus.CREATED));

    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("token") String token, HttpServletRequest httpServletRequest)
    {
        try {
            userService.verifyAccountToken(token);
            return ResponseEntity.ok().body(RequestUtil.getResponse(httpServletRequest, emptyMap(), "Account Verified.", HttpStatus.OK));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(RequestUtil.getResponse(httpServletRequest, emptyMap(), e.getMessage(), HttpStatus.BAD_REQUEST));
        }
    }


    private URI getUri()
    {
        return URI.create("");
    }

    @GetMapping("/testagain")
    public ResponseEntity<Response> testagain()
    {
        return ResponseEntity.ok().build();
    }
    @PostMapping("/test")
    public ResponseEntity<Response> test()
    {
        return ResponseEntity.ok().build();
    }

    // The authentication mechanism has been moved to project's filter class so that
    // requests can be intercepted and authenticated there separately

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserRequest userRequest)
//    {
//        //When user request comes to controller it's still un-authenticated, so we'll call unauthenticated method
//        //so that user can be verifiedl
//        //Instead of authenticated/unauthenticated method we can directly use UsernamePasswordAuthenticationToken constructor as well
//        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(userRequest.getEmail(), userRequest.getPassword());
//        Authentication authentication =authenticationManager.authenticate(unauthenticated);
//        return ResponseEntity.ok().body(Map.of("user",authentication));
//    }
}
