package com.example.ChatApp.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Builder;

import com.example.ChatApp.Config.Utility;
import com.example.ChatApp.Models.Submodels.LastMessage_MsgGroup;

@Document("Message_groups")
@Builder
public class Message_groups {
	@Id
	public String _id;
	public String Message_group_name;
	public String Message_group_image;
	public LastMessage_MsgGroup Last_message;
	public String MsgGroupType; // có 2 loại, 1 là group thông thường, 2 là Individual (này là tượng trưng cho nhắn tin cá nhân)
	public String MsgConnectedId;
	public Boolean isDeleted = false;

	/*
	 * public Message_groups(String _id, String message_group_name, String
	 * message_group_image, LastMessage_MsgGroup last_message) { this._id = _id;
	 * Message_group_name = message_group_name; Message_group_image =
	 * message_group_image; Last_message = last_message; }
	 */
	public Message_groups(String message_group_name, String message_group_image, LastMessage_MsgGroup last_message, String groupType ) {
		super();
		Message_group_name = message_group_name;
		Message_group_image = message_group_image;
		Last_message = last_message;
		MsgGroupType = groupType;
		MsgConnectedId = null;
		isDeleted = false;
	}

	public Message_groups(String message_group_name, String message_group_image, LastMessage_MsgGroup last_message, String groupType,
			String msgConnectedId ) {
		super();
		Message_group_name = message_group_name;
		Message_group_image = message_group_image;
		Last_message = last_message;
		MsgGroupType = groupType;
		MsgConnectedId = msgConnectedId;
		isDeleted = false;
	}

	public Message_groups() {
        
    }

}
