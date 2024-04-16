package com.example.ChatApp.Models.Submodels;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class MessageGroup_User {
	@Field(targetType = FieldType.OBJECT_ID)
	public String messageGroupId;
	public boolean isRead;
	public String role;
	public MessageGroup_User() {
		
	}
	public MessageGroup_User(String messageGroupId, boolean isRead, String role) {
		super();
		this.messageGroupId = messageGroupId;
		this.isRead = isRead;
		this.role = role;
	}

}
