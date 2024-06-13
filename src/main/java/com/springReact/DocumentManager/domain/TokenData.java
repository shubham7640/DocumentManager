package com.springReact.DocumentManager.domain;

import com.springReact.DocumentManager.dto.User;
import io.jsonwebtoken.Claims;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Builder
@Getter
@Setter
public class TokenData {
    private User user;

    // Claims class is being imported from the new java jwt dependency
    private Claims claims;

    private boolean valid;

    private List<GrantedAuthority> authorities;

}
