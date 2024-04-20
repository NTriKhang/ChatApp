
package com.example.ChatApp.Services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.ChatApp.Config.Utility;
import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Users;
import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.UsersRepository;
import com.example.ChatApp.dto.CreateGroupDTO;
import com.example.ChatApp.dto.MessageGroupUpdateDto;
import com.example.ChatApp.dto.UserGroupDto;
import com.mongodb.client.result.UpdateResult;

import io.jsonwebtoken.io.IOException;

@Service
public class MessageGroupService {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private MessageGroupsRepository messageGroupsRepository;
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
	
	public String create_GroupString(CreateGroupDTO request) {
		ObjectId obj_ID_LOGIN = new ObjectId(request.userCreatedId);
		// HANDLE
		System.out.println(request.MsgConnectedId);
		Message_groups newGroup;
		if(request.MsgConnectedId != "") {
				newGroup = new Message_groups(
					request.groupName,
					"",
					new LastMessage_MsgGroup(),
					"Individual",
					request.MsgConnectedId
				);
		}
		else {
				newGroup = new Message_groups(
					request.groupName,
					"",
					new LastMessage_MsgGroup(),
					"Group"
				);
		}
		Message_groups savedGroup = messageGroupsRepository.save(newGroup);
		List<ObjectId> userList = request.userList;
		
		// save admin = acc nao dang nhap la admin khi tao
		Users userlogin = usersRepository.findById(obj_ID_LOGIN).orElse(null);
		MessageGroup_User messageGroupUser_Login = new MessageGroup_User();
		messageGroupUser_Login.messageGroupId = savedGroup._id;
		userlogin.List_message_group.add(messageGroupUser_Login);
		messageGroupUser_Login.role = "Admin";
		usersRepository.save(userlogin);


		// list user 
		for (ObjectId userId : userList) {
			Users user = usersRepository.findById(userId).orElse(null);
			
			if(user != null) {
				MessageGroup_User messageGroupUser = new MessageGroup_User();
				messageGroupUser.messageGroupId = savedGroup._id;
				user.List_message_group.add(messageGroupUser);
				messageGroupUser.role = "Participant";
				usersRepository.save(user);
			}
		}
		return null;
	}

}
