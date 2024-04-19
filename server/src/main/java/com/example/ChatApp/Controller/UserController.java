package com.example.ChatApp.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("{userId}")
	public ResponseEntity<Users> getUserById(@PathVariable("userId") String userId) {
	    Users user = userService.findUserById(userId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	    return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@GetMapping("/findUser/{tag}")
	public ResponseEntity<List<Users>> getListUserByTag(
				@PathVariable("tag") String tag
			) {
		List<Users> listUser = userService.findUsersByTag(tag)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return new ResponseEntity<>(listUser, HttpStatus.OK);
	}
}
