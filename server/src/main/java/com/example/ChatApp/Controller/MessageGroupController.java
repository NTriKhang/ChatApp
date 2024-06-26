package com.example.ChatApp.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Repositories.MessageGroupsRepository;
import com.example.ChatApp.Services.MessageGroupService;
import com.example.ChatApp.Services.MessageService;
import com.example.ChatApp.Services.SocketService;
import com.example.ChatApp.SocketDto.CreateGroupDTO;
import com.example.ChatApp.dto.DeleteGroupRequestDto;
import com.example.ChatApp.dto.ImageStringDto;
import com.example.ChatApp.dto.MessageGroupUpdateDto;
import com.example.ChatApp.dto.UserGroupDto;
import com.mongodb.client.result.UpdateResult;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/message_group")
public class MessageGroupController {
	@Autowired
	private MessageGroupService messageGroupService;
	@Autowired
	private SocketService socketService;

	/*
	 * @Autowired private MessageGroupsRepository messageGroupsRepository;
	 */

	@GetMapping("/{userID}")
	public ResponseEntity<List<UserGroupDto>> getListMessGroupByUserID(
			@PathVariable("userID") String userID,
			@RequestParam(value = "isConnected", defaultValue = "false") boolean isConnected) {
		try {
			return new ResponseEntity<>(messageGroupService.getListMessGroupByUserID(userID, isConnected),
					HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return new ResponseEntity<>(new ArrayList<UserGroupDto>(), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping
	public ResponseEntity<MessageGroupUpdateDto> UpdateMessageGroup(
			@RequestBody MessageGroupUpdateDto messageGroupUpdate) {
		// System.out.println("ID: " + GroupID);
		ObjectId id = new ObjectId(messageGroupUpdate._id);
		UpdateResult res = messageGroupService.update_NAME_Message_groups(messageGroupUpdate);
		if (res.wasAcknowledged()) {
			return ResponseEntity.ok(messageGroupUpdate);
		}
		return new ResponseEntity<MessageGroupUpdateDto>(HttpStatus.BAD_REQUEST);
	}

	// /api/v1/message_groupUpload_Images/
	@PostMapping("Upload_Images/{GroupID}")
	public ResponseEntity<?> UploadIMG(@PathVariable String GroupID, @RequestBody ImageStringDto imageUrl)
			throws IOException {
		System.out.println(GroupID);
		UpdateResult res = messageGroupService.uploadImageMessageGroup(GroupID, imageUrl.imageUrl);
		if (res.wasAcknowledged())
			return new ResponseEntity<>(HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	@MessageMapping("/CreateGroup")
	public ResponseEntity<?> CreateGroupMessages(@Payload CreateGroupDTO createGroupRequest) {
		try {
			Message_groups result = messageGroupService.create_GroupString(createGroupRequest);
			if (result == null) {
				socketService.sendErrorToUser(createGroupRequest.userCreatedId);
			} else {

				SocketService.addUserToGroup(createGroupRequest.userCreatedId, result._id);
				socketService.sendNotifyToUser(createGroupRequest.userCreatedId, result);

				for (ObjectId userId : createGroupRequest.userList) {
					SocketService.addUserToGroup(userId.toString(), result._id);
					socketService.sendNotifyToUser(userId.toString(), result);

				}
			}
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
		}

	}

	@MessageMapping("/deleteGroup")
	public ResponseEntity<?> DeleteGroup(
			@Payload DeleteGroupRequestDto message_groups) {
		try {
			UpdateResult result = messageGroupService.deleteGroup(message_groups, message_groups.userId);
			if (result.wasAcknowledged()) {
				socketService.sendNotifyDeleteGroupToUser(message_groups.userId, message_groups);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
		}
	}
}
