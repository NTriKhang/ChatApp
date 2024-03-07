package com.example.ChatApp.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Services.MessageService;

@RestController
@RequestMapping("/api/v1/messages")
public class MessagesController {
	@Autowired
	private MessageService messageService;
	
	@PostMapping
	public ResponseEntity<Messages> create(@RequestBody Messages messages){
		try {
			messages = messageService.insertOne(messages);
			return new ResponseEntity<>(messages, HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			// TODO: handle exception
		}
	}
	
	@GetMapping()
	public ResponseEntity<List<Messages>> getAll(){

		System.out.println("Get all messages in controller");
			List<Messages> messages = messageService.getAll();
			return new ResponseEntity<List<Messages>>(messages, HttpStatus.OK);
	}
}
