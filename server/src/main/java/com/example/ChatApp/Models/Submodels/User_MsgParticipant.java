package com.example.ChatApp.Models.Submodels;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class User_MsgParticipant {
	@Field(targetType = FieldType.OBJECT_ID)
	public String user_id;
	public String user_name;
	public String tag;
	public LocalDateTime join_date;
	public String role;
	
	public User_MsgParticipant(String user_id, String user_name, String tag, LocalDateTime join_date, String role) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.tag = tag;
		this.join_date = join_date;
		this.role = role;
	}
}
