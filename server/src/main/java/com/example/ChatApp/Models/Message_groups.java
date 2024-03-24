package com.example.ChatApp.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;

import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;

@Document("Message_groups")
@Builder
public class Message_groups {
	@Id
	public String _id;
	public String Message_group_name;
	public String Message_group_image;
	public LastMessage_MsgGroup Last_message;

	public Message_groups(String _id, String message_group_name, String message_group_image,
			LastMessage_MsgGroup last_message) {
		this._id = _id;
		Message_group_name = message_group_name;
		Message_group_image = message_group_image;
		Last_message = last_message;
	}
	public Message_groups() {
        
    }
	
}
