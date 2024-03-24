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
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getMessage_group_name() {
		return Message_group_name;
	}
	public void setMessage_group_name(String message_group_name) {
		Message_group_name = message_group_name;
	}
	public String getMessage_group_image() {
		return Message_group_image;
	}
	public void setMessage_group_image(String message_group_image) {
		Message_group_image = message_group_image;
	}
	public LastMessage_MsgGroup getLast_message() {
		return Last_message;
	}
	public void setLast_message(LastMessage_MsgGroup last_message) {
		Last_message = last_message;
	}
	

	
	
	
}
