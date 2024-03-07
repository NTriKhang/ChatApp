package com.example.ChatApp.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;

@Document("Message_groups")
public class Message_groups {
	@Id
	public ObjectId _id;
	public String Message_group_name;
	public String Message_group_image;
	public LastMessage_MsgGroup Last_message;
	
}
