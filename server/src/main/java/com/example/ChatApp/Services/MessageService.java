package com.example.ChatApp.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Repositories.MessageRepository;

@Service
public class MessageService {
	@Autowired
	protected MessageRepository messageRepository;
	
	private final MongoTemplate mongoTemplate;

	public MessageService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
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
	public List<Messages> getMessagesByGroupId(String group_id, String userId, int page) {
		ObjectId objectId = new ObjectId(group_id);
		int limit = 20;
		int skip = (page - 1) * limit;
		System.out.println(limit + " " + skip + " " + userId);
		List<Messages> messages = messageRepository.findMessagesByGroupId(objectId, userId, skip, limit);
		return messages;
	}
	
	public Messages deleteMessagesById(String messageId, String userId) {
		ObjectId objectId = new ObjectId(messageId);
		Messages message = messageRepository.findMessageById(objectId);
		if(message.Sender_user.user_id.equals(userId)) {
			return messageRepository.deleteMessageByIdOfOwner(objectId);
		}
		else {
			Query query = Query.query(Criteria.where("_id").is(messageId));
		    Update update = new Update().push("Unseen", userId);
		    mongoTemplate.updateFirst(query, update, Messages.class);
			return message;
		}
	}
}
