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
import com.example.ChatApp.Models.Submodels.MessageGroup_User;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Repositories.UsersRepository;
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
					Message_groups msGroups = messageGroup.get();
					rs.add(new UserGroupDto(msGroups._id, msGroups.Message_group_name,
							Utility.FilePath.GroupImagePath + msGroups.Message_group_image,
							msGroups.Last_message, user_messGroup.isRead, user_messGroup.role));

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
}
