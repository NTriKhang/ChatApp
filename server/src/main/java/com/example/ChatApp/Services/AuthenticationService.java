package com.example.ChatApp.Services;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.dto.SignUpDto;

public interface AuthenticationService {
    Users signup(SignUpDto sigUpRequest);
}
