package com.example.ChatApp.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ChatApp.Models.Message_participants;
import com.example.ChatApp.Services.MessageParticipantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1")

public class MessagesParticipantsController {
    

    @Autowired
	private MessageParticipantService messageServiceMessageParticipantsService;

    @GetMapping("/messagesparticipants")
	public ResponseEntity<List<Message_participants>> getAll(){

			List<Message_participants> messagesParticipants = messageServiceMessageParticipantsService.getAllMessage_participants();
			return new ResponseEntity<List<Message_participants>>(messagesParticipants, HttpStatus.OK);
	}
	
	
}
