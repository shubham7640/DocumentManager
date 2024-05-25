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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.util.Collections.emptyMap;


@RestController
@RequiredArgsConstructor //No need of autowired after this as we have already initialized the params
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid UserRequest userRequest, HttpServletRequest httpServletRequest)
    {
        userService.createUser(userRequest.getFirstName(),userRequest.getLastName(),userRequest.getEmail(),userRequest.getPassword());
        return ResponseEntity.created(getUri()).body(RequestUtil.getResponse(httpServletRequest,emptyMap(),"Account Created. Please check your email to enable your account", HttpStatus.CREATED));

    }

    private URI getUri()
    {
        return URI.create("");
    }


}
