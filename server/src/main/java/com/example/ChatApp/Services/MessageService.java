package com.example.ChatApp.Services;

import java.lang.foreign.Linker.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.MessageRepository;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.SocketDto.MessageTextDto;
import com.example.ChatApp.SocketDto.MessageTextIndDto;

@Service
public class MessageService {
	@Autowired
	protected MessageRepository messageRepository;
	@Autowired
	protected UsersRepository usersRepository;
	@Autowired
	protected MessageGroupsRepository messageGroupsRepository;
	private final MongoTemplate mongoTemplate;

	public MessageService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	public Messages insertOne(MessageTextDto messageTextDto) {
			Messages messages = new Messages(messageTextDto.Content, messageTextDto.Message_group_id, messageTextDto.Sender_user);
			messages = messageRepository.save(messages);
			return messages;		

	}
	/**
	 * Hàm trả về 1 List 2 phần tử
	 * phần tử đầu là tin nhắn của người gửi đi
	 * phần từ sau là tin nhắn của người nhận */
	public List<Messages> InitPrivateMessage(MessageTextIndDto messageTextIndDto) {
		Optional<Users> senderUser = usersRepository.find_By(messageTextIndDto.SenderId);
		Optional<Users> receiveUser = usersRepository.find_By(messageTextIndDto.ReceiverId);
		
		if(!senderUser.isPresent() || !receiveUser.isPresent())
			return  null;
		Message_groups senderGroups = new Message_groups(senderUser.get().Display_name, senderUser.get().Background_image_path, null);
		Message_groups receiveGroups = new Message_groups(receiveUser.get().Display_name, receiveUser.get().Background_image_path, null);
		try {
			senderGroups = messageGroupsRepository.save(senderGroups);
			receiveGroups = messageGroupsRepository.save(receiveGroups);
			return insertBothMessage(senderGroups._id, receiveGroups._id, messageTextIndDto.Content);
			
		} catch (Exception e) {
			System.out.println(e.getMessage()); 
			System.err.println(e.getStackTrace());
			return null;
		}
		
	}
	public List<Messages> insertBothMessage(String senderGroupId, String receiverGroupId, String Content) {
		List<Messages> messageList = new ArrayList<Messages>();
		Messages messageSender = insertPrivateMessage(senderGroupId, Content);
		if(messageSender == null)
			return null;
		Messages messageReceiver = insertPrivateMessage(receiverGroupId, Content);
		if(messageReceiver == null)
			return null;
		messageList.add(messageSender);
		messageList.add(messageReceiver);
		return messageList;
	}
	private Messages insertPrivateMessage(String groupId, String content) {
		Messages messages = new Messages(content, groupId);
		try {
			messages = messageRepository.save(messages);
			return messages;
		} catch (Exception e) {
			System.out.println(e.getMessage()); 
			System.err.println(e.getStackTrace());
			return null;
		}
	}
	public List<Messages> getAll(){
		System.out.println("Get all messages in services");
		List<Messages> messages = messageRepository.findAll();

		return messages;
	}
	public List<Messages> getMessagesByGroupId(String group_id, String userId, int page) {
		ObjectId objectId = new ObjectId(group_id);
		int limit = 20;
		int skip = (page - 1) * limit;

		List<Messages> messages = messageRepository.findMessagesByGroupId(objectId, userId, skip, limit);
		return messages;
	}
	
	public Messages deleteMessagesById(String messageId, String userId) {
		ObjectId objectId = new ObjectId(messageId);
		Messages message = messageRepository.findMessagesById(objectId);
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
