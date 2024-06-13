package com.springReact.DocumentManager.service;

import com.springReact.DocumentManager.domain.Token;
import com.springReact.DocumentManager.domain.TokenData;
import com.springReact.DocumentManager.dto.User;
import com.springReact.DocumentManager.enumeration.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.util.Optional;
import java.util.function.Function;

public interface JwtService {

    String createToken(User user, Function<Token,String> tokenFunction);
    Optional<String> extractToken(HttpServletRequest request, String tokenType);
    void addCookie(HttpServletResponse response, User user, TokenType tokenType);
    <T> T getTokenData(String token, Function<TokenData,T> tokenFunction);

}
