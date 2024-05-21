
package com.example.ChatApp.Services;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Config.Utility;
import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.SocketDto.CreateGroupDTO;
import com.example.ChatApp.dto.DeleteGroupRequestDto;
import com.example.ChatApp.dto.MessageGroupUpdateDto;
import com.example.ChatApp.dto.UserGroupDto;
import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;

import io.jsonwebtoken.io.IOException;

@Service
public class MessageGroupService {
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private MessageGroupsRepository messageGroupsRepository;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	public String root = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\";
  
  public Optional<Message_groups> getMsgGroupById(String msgId){
		return messageGroupsRepository.findById(new ObjectId(msgId));
	}
  
	public List<UserGroupDto> getListMessGroupByUserID(String userID, Boolean isConnect) throws Exception {

		ObjectId id = new ObjectId(userID);
		Optional<Users> users = usersRepository.findById(id);

		List<UserGroupDto> rs = new ArrayList<>();

		if (users.isPresent()) {

			List<MessageGroup_User> listGroupMessIds = users.get().List_message_group;

			listGroupMessIds.forEach(user_messGroup -> {

				ObjectId msgId = new ObjectId(user_messGroup.messageGroupId);
				Optional<Message_groups> messageGroup = messageGroupsRepository.findById(msgId);

				if (messageGroup.isPresent()) {
					//String receiverId = "";
					Message_groups msGroups = messageGroup.get();
					/*
					 * System.out.println(msGroups.MsgGroupType);
					 * 
					 * if(msGroups.MsgGroupType.equals(Utility.MsgGroupType.Individual)) {
					 * Optional<Users> receiverUser = usersRepository.findByMsgGroupId(new
					 * ObjectId(msGroups.MsgConnectedId)); if(!receiverUser.isPresent()) return;
					 * receiverId = receiverUser.get()._id; }
					 */
					
					rs.add(new UserGroupDto(msGroups._id, msGroups.Message_group_name,
							msGroups.Message_group_image,
							msGroups.Last_message, user_messGroup.isRead, user_messGroup.role, msGroups.MsgGroupType, user_messGroup.receiverId));

				}
			});
		}
		if(!isConnect && rs.size() > 0) {
			SocketService.initUserConnectAsync(userID, rs)
		    .thenRun(() -> {
		        // Any code to execute after the operation completes
		        System.out.println("InitUserConnect operation completed asynchronously.");
		    })
		    .exceptionally(ex -> {
		        // Handle any exceptions that occurred during the operation
		        System.err.println("Error occurred: " + ex.getMessage());
		        return null; // Return null to allow the chain to continue
		    });
		}
		java.util.Collections.sort(rs, java.util.Collections.reverseOrder( Comparator.comparing(obj -> obj.Last_message.created_date)));
		
		return rs;
	}

	public UpdateResult update_NAME_Message_groups(MessageGroupUpdateDto messageGroup) {
		ObjectId id = new ObjectId(messageGroup._id);
		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update().set("Message_group_name", messageGroup.Message_group_name);

		UpdateResult result = mongoTemplate.updateFirst(query, update, Message_groups.class);

		return result;
	}

	public UpdateResult uploadImageMessageGroup(String MessGR_ID, String imageUrl)
			throws IOException, IllegalStateException, java.io.IOException {

		ObjectId id = new ObjectId(MessGR_ID);
		Optional<Message_groups> messgrs = messageGroupsRepository.findById(id);
		if (!messgrs.isPresent())
			return null;

		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update().set("Message_group_image", imageUrl);

		UpdateResult result = mongoTemplate.updateFirst(query, update, Message_groups.class);
		return result;

	}
	
	public Message_groups create_GroupString(CreateGroupDTO request) {
		ObjectId createdUserId = new ObjectId(request.userCreatedId);
		
		Message_groups newGroup;

		newGroup = new Message_groups(request.groupName, "", new LastMessage_MsgGroup(LocalDateTime.now()), Utility.MsgGroupType.Group);

		Message_groups savedGroup = messageGroupsRepository.save(newGroup);
		
		List<ObjectId> userList = request.userList;
		
	 	UpdateResult result = addUserGroup(createdUserId, new MessageGroup_User(savedGroup._id, true, Utility.Role.Admin, null));
		if(!result.wasAcknowledged())
			return null;

		// list user 
		for (ObjectId userId : userList) {
			result = addUserGroup(userId, new MessageGroup_User(savedGroup._id, true, Utility.Role.Participant, null));
			if(!result.wasAcknowledged())
				return null;
		}
		return savedGroup;
	}
	private UpdateResult addUserGroup(ObjectId userId, MessageGroup_User messageGroup_User) {
		Query query = new Query(Criteria.where("_id").is(userId));
		Update update = new Update();
		update.push("List_message_group", messageGroup_User);
		return mongoTemplate.updateFirst(query, update, Users.class);
	}
	
	public UpdateResult deleteGroup(DeleteGroupRequestDto message_groups, String userId) {
		ObjectId id = new ObjectId(message_groups.GroupId);
		Query query = new Query(Criteria.where("_id").is(userId));
		Update update = new Update().pull("List_message_group", new BasicDBObject("messageGroupId", id));
		mongoTemplate.updateFirst(query, update, Users.class);
		
		if(message_groups.MsgGroupType.equals("Individual")) {
			query = new Query(Criteria.where("_id").is(id));
			update = new Update().set("isDeleted", true);
			
			return mongoTemplate.updateFirst(query, update, Message_groups.class);
		}
		else if(message_groups.MsgGroupType.equals("Group")) {			
			List<Messages> lstMsg = messageService.getMessagesByGroupId(message_groups.GroupId, userId, 1);
			List<ObjectId> lstId = lstMsg.stream()
					.map(Messages -> new ObjectId(Messages._id))
					.collect(Collectors.toList());
			query = new Query(Criteria.where("_id").in(lstId));
			update = new Update().push("Unseen", userId);
			return mongoTemplate.updateMulti(query, update, Messages.class);
		}
		else {
			return null;
		}
	}
}
