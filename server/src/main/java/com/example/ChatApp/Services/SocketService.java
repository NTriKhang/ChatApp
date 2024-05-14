
package com.example.ChatApp.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Message_groups;
import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.SocketDto.MessageTextDto;
import com.example.ChatApp.dto.DeleteGroupRequestDto;
import com.example.ChatApp.dto.UserGroupDto;

@Service
public class SocketService {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	public static Map<String, List<String>> groupMembers = new HashMap<>();

	public static void addUserToGroup(String userId, String groupId) {
		if(!groupMembers.containsKey(groupId)) {
            List<String> userList = new ArrayList<>();
            userList.add(userId);
            groupMembers.put(groupId, userList);
		}
		else {
        	if(!groupMembers.get(groupId).contains(userId))
        		groupMembers.get(groupId).add(userId);
        }
	}
	//only call in message group service when get all the message groups of user, CompletableFuture is for async, response doesn't need to wait for this
	public static CompletableFuture<Void> initUserConnectAsync(String userId, List<UserGroupDto> messageGroupIds) {
	    return CompletableFuture.runAsync(() -> {
	        for(UserGroupDto msg : messageGroupIds) {
	        	addUserToGroup(userId, msg.MessageGroupId);
	        }
	    });

	}
	public void sendMessageToGroup(String groupId, Messages message) {
		List<String> memberId = groupMembers.get(groupId);
        if (memberId != null) {
            for (String id : memberId) {
                // Send message to each member in group
            	simpMessagingTemplate.convertAndSendToUser(id, "/message_group", message);
            }
        }
	}
	public void sendPrivateMessage(String senderId, String receiverId, Messages messageSender, Messages messageReceiver) {
		simpMessagingTemplate.convertAndSendToUser(senderId, "/message", messageSender);
		simpMessagingTemplate.convertAndSendToUser(receiverId, "/message", messageReceiver);
	}
	public void sendNotifyToUser(String userId, Message_groups group) {
		simpMessagingTemplate.convertAndSendToUser(userId, "/notify", group);
	}
	public void sendErrorToUser(String userId) {
		simpMessagingTemplate.convertAndSendToUser(userId, "/error", "Something mighr wrong, error occur");
	}
	public void removeUserFromGroup(String username, String groupName) {
		List<String> members = groupMembers.get(groupName);
		if (members != null) {
			members.remove(username);
			if (members.isEmpty()) {
				groupMembers.remove(groupName);
			}
		}
	}
	public void sendNotifyDeleteGroupToUser(String userId, DeleteGroupRequestDto groupId) {
		simpMessagingTemplate.convertAndSendToUser(userId, "/notify", groupId);
	}
}
