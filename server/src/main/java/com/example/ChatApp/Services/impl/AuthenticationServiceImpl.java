package com.example.ChatApp.Services.impl;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.Services.AuthenticationService;
import com.example.ChatApp.dto.SigUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthenticationServiceImpl implements AuthenticationService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    public Users sigup(SigUpRequest sigUpRequest){
        Users users=new Users();
        users.setEmail(sigUpRequest.getEmail());
        users.setDisplay_name(sigUpRequest.getDisplay_Name());
        users.setPassword(passwordEncoder.encode(sigUpRequest.getPassword()));
        return usersRepository.save(users);
    }
}
