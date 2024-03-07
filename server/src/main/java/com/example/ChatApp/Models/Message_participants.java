package com.example.ChatApp.Models;

import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.example.ChatApp.Models.Submodels.User_MsgParticipant;

@Document("Message_participants")
public class Message_participants {
	@Id
	public String _id;
	@Field(targetType = FieldType.OBJECT_ID)
	public User_MsgParticipant User;
	public String Message_group_id;
	
	
}
