package com.example.ChatApp.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Message_participants;

import com.example.ChatApp.Repositories.MessageParticipantsRepository;

@Service
public class MessageParticipantService {
    @Autowired
    protected MessageParticipantsRepository messagesParticipantsRepository;

    public List<Message_participants> getAllMessage_participants(){
		System.out.println("SERVIERS / MESSAGE PARTICIPANT SERVIERS");
		List<Message_participants> messagesparticipants = messagesParticipantsRepository.findAll();
		System.out.println(messagesParticipantsRepository.findAll());
		return messagesparticipants;
	}
}
