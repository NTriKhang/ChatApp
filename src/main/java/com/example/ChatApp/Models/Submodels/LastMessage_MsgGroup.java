package com.example.ChatApp.Models.Submodels;

import java.sql.Date;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class LastMessage_MsgGroup {
	@Field(targetType = FieldType.OBJECT_ID)
	public String message_id;
	public String content;
	public String user_name;
	public Date created_date;
	
	public LastMessage_MsgGroup(String message_id, String content, String user_name, Date created_date) {
		super();
		this.message_id = message_id;
		this.content = content;
		this.user_name = user_name;
		this.created_date = created_date;
	}
}
