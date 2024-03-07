package com.example.ChatApp.Controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ChatApp.Models.Message_groups;

import com.example.ChatApp.Services.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/get-mess-group-by-user-id/{userID}")
	public ResponseEntity< List<Message_groups>> getListMessGroupByUserID(@PathVariable("userID") String userID) {
		
        try {
            
            return new ResponseEntity<>(userService.getListMessGroupByUserID(userID), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            return new ResponseEntity<>(new ArrayList<Message_groups>(), HttpStatus.BAD_REQUEST);
        }
	}
}
