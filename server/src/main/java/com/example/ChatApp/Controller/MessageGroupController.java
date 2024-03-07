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
import com.example.ChatApp.Services.MessageGroupService;
import com.example.ChatApp.dto.UserGroupDto;

@RestController
@RequestMapping("/api/v1/message_group")
public class MessageGroupController {
	@Autowired
	private MessageGroupService messageGroupService;
	
	@GetMapping("/{userID}")
	public ResponseEntity<List<UserGroupDto>> getListMessGroupByUserID(@PathVariable("userID") String userID) {
        try {
            System.out.println("in get list");
            return new ResponseEntity<>(messageGroupService.getListMessGroupByUserID(userID), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            return new ResponseEntity<>(new ArrayList<UserGroupDto>(), HttpStatus.BAD_REQUEST);
        }
	}
}
