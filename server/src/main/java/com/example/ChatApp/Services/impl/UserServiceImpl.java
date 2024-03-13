package com.example.ChatApp.Services.impl;

import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return usersRepository.findByEmail(username)
                        .orElseThrow(()->new UsernameNotFoundException("User not found"));
            }
        };
    }
}
