package com.example.ChatApp.dto;

import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;

public class UserGroupDto {
	public String MessageGroupId;
	public String Message_group_name;
	public String Message_group_image;
	public LastMessage_MsgGroup Last_message;
	public Boolean Is_read;
	public String Role;
	public String Message_group_type;
	public String ReceiverId;
	
	public UserGroupDto(String messageGroupId, String message_group_name, String message_group_image,
			LastMessage_MsgGroup last_message, Boolean is_read, String role, String groupType, String receiverId) {
		super();
		this.MessageGroupId = messageGroupId;
		Message_group_name = message_group_name;
		Message_group_image = message_group_image;
		Last_message = last_message;
		Is_read = is_read;
		Role = role;
		Message_group_type = groupType;
		ReceiverId = receiverId;
	}
}
