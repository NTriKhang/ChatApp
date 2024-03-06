package com.example.ChatApp.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Repositories.MessageRepository;

@Service
public class MessageService {
	@Autowired
	protected MessageRepository messageRepository;
	
	public Messages insertOne(Messages messages) {
		messages.Created_date = LocalDateTime.now();
		messages = messageRepository.save(messages);
		return messages;
	}
	public List<Messages> getAll(){
		System.out.println("Get all messages in services");
		List<Messages> messages = messageRepository.findAll();
		System.out.println(messages.size());
		return messages;
	}
	public List<Messages> getMessagesByGroupId(String group_id) {
		ObjectId objectId = new ObjectId(group_id);
		List<Messages> messages = messageRepository.findMessagesByGroupId(objectId);
		return messages;
	}
}
