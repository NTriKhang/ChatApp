package com.example.ChatApp.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.AbstractBsonWriter;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import com.example.ChatApp.Config.Utility;
import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Models.Submodels.SenderUser_Msg;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.MessageRepository;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.SocketDto.MessageTextDto;
import com.example.ChatApp.SocketDto.MessageTextIndDto;
import com.example.ChatApp.dto.MsgGroupIdDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.mongodb.client.result.UpdateResult;

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
			updateLastMessage(messageTextDto.Message_group_id, new LastMessage_MsgGroup(messages._id, messageTextDto.Content, messageTextDto.Sender_user.user_name, LocalDateTime.now()));
			return messages;		

	}
	private UpdateResult updateLastMessage(String msgGroupId, LastMessage_MsgGroup lastMessage_MsgGroup) {
		ObjectId id = new ObjectId(msgGroupId);
		Query query = Query.query(Criteria.where("_id").is(id));
		Update update = new Update().set("Last_message", lastMessage_MsgGroup);
		return mongoTemplate.updateFirst(query, update, Message_groups.class);
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
		Message_groups senderGroups = new Message_groups(receiveUser.get().Display_name, receiveUser.get().Image_path, null, Utility.MsgGroupType.Individual);
		Message_groups receiveGroups = new Message_groups(senderUser.get().Display_name, senderUser.get().Image_path, null, Utility.MsgGroupType.Individual);
		try {
			senderGroups = messageGroupsRepository.save(senderGroups);
			addGroupIdToListGroupOfUser(senderGroups._id, senderUser.get()._id, receiveUser.get()._id, true);
		
			receiveGroups = messageGroupsRepository.save(receiveGroups);
			addGroupIdToListGroupOfUser(receiveGroups._id, receiveUser.get()._id, senderUser.get()._id, false);
			
			updateConnectedMsgGroup(senderGroups._id, receiveGroups._id);
			updateConnectedMsgGroup(receiveGroups._id, senderGroups._id);
			
			System.out.println("return");
			return insertBothMessage(new SenderUser_Msg(senderUser.get()._id, senderUser.get().Display_name) ,senderGroups._id, receiveGroups._id, messageTextIndDto.Content);	
		} catch (Exception e) {
			System.out.println(e.getMessage()); 
			System.err.println(e.getStackTrace());
			return null;
		}
		
	}
	
	private void updateConnectedMsgGroup(String groupdId1, String groupdId2) {
		
		ObjectId id = new ObjectId(groupdId1);
		Query query = Query.query(Criteria.where("_id").is(id));
		Update update = new Update().set("MsgConnectedId", new ObjectId(groupdId2));
		mongoTemplate.updateFirst(query, update, Message_groups.class);
	}
	/**
	 * Nếu là nhóm nhiều người thì receiverId set = null */
	private void addGroupIdToListGroupOfUser(String groupId, String userId, String receiverId, Boolean isSender) {
		ObjectId id = new ObjectId(userId);
		Query query = Query.query(Criteria.where("_id").is(id));
		MessageGroup_User messageGroup_User = new MessageGroup_User(groupId, isSender, Utility.Role.Participant, receiverId);
		Update update = new Update().push("List_message_group", messageGroup_User);
		mongoTemplate.updateFirst(query, update, Users.class);
	}
	
	public List<Messages> insertBothMessage(SenderUser_Msg sender, String senderGroupId, String receiverGroupId, String Content) {
		List<Messages> messageList = new ArrayList<Messages>();
		Messages messageSender = insertPrivateMessage(senderGroupId, Content, sender);
		if(messageSender == null)
			return null;
		Messages messageReceiver = insertPrivateMessage(receiverGroupId, Content, sender);
		if(messageReceiver == null)
			return null;
		messageList.add(messageSender);
		messageList.add(messageReceiver);
		return messageList;
	}
	private Messages insertPrivateMessage(String groupId, String content, SenderUser_Msg sender) {
		Messages messages = new Messages(content, groupId, sender);
		try {
			messages = messageRepository.save(messages);
			updateLastMessage(groupId, new LastMessage_MsgGroup(messages._id, content, sender.user_name, LocalDateTime.now()));
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
		List<Messages> subMessage = new ArrayList<>();
		int limit = 20;
		int skip = (page - 1) * limit;

		List<Messages> messages = messageRepository.findMessagesByGroupId(objectId, skip, limit);
		for(Messages message : messages) {
			if(!message.Unseen.contains(userId)) {
				subMessage.add(message);
			}
		}
		return subMessage;
	}
	public List<Messages> getMessagesByReceiverId(String receiverId, String userId, int page) {
		Optional<MsgGroupIdDto> msgOptional = usersRepository.findByReceiverIdAndUserId(new ObjectId(receiverId), new ObjectId(userId));
		if(msgOptional.isEmpty())
			return null;
		
		ObjectId objectId = new ObjectId(msgOptional.get().messageGroupId);
		int limit = 20;
		int skip = (page - 1) * limit;

		List<Messages> messages = messageRepository.findMessagesByGroupId(objectId, skip, limit);
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
