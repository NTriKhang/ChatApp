package com.example.ChatApp.Services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String extractUserName(String token);
    String generateToken(UserDetails userDetails);
    public boolean isTokenValid(String token,UserDetails userDetails);
}
