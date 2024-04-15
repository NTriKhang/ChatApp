
package com.example.ChatApp.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.ChatApp.Models.Messages;
import com.example.ChatApp.SocketDto.MessageTextDto;
import com.example.ChatApp.dto.UserGroupDto;

@Service
public class SocketService {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	public static Map<String, List<String>> groupMembers = new HashMap<>();

	public void addUserToGroup(String username, String groupName) {
		groupMembers.computeIfAbsent(groupName, k -> new ArrayList<>()).add(username);
	}
	//only call in message group service when get all the message groups of user, CompletableFuture is for async, response doesn't need to wait for this
	public static CompletableFuture<Void> initUserConnectAsync(String userId, List<UserGroupDto> messageGroupIds) {
	    return CompletableFuture.runAsync(() -> {
	        for(UserGroupDto msg : messageGroupIds) {
	            if(!groupMembers.containsKey(msg.MessageGroupId)) {
	                List<String> userList = new ArrayList<>();
	                userList.add(userId);
	                groupMembers.put(msg.MessageGroupId, userList);
	            } else {
	            	if(!groupMembers.get(msg.MessageGroupId).contains(userId))
	            		groupMembers.get(msg.MessageGroupId).add(userId);
	            }
	        }
	    });

	}
	public void sendMessageToGroup(String groupId, Messages message) {
		List<String> memberId = groupMembers.get(groupId);
        if (memberId != null) {
            for (String id : memberId) {
                // Send message to each member of the group
            	simpMessagingTemplate.convertAndSendToUser(id, "/message_group", message);
            }
        }
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
}
