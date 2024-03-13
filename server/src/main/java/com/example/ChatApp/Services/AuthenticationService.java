package com.example.ChatApp.Services;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.dto.SigUpRequest;

public interface AuthenticationService {
    Users sigup(SigUpRequest sigUpRequest);
}
