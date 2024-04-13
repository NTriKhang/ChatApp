package com.example.ChatApp.Models.Submodels;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class SenderUser_Msg {
	@Field(targetType = FieldType.OBJECT_ID)
	public String user_id;
	public String user_name;
	
	public SenderUser_Msg() {
	}

	public SenderUser_Msg(String user_id, String user_name) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
	}
	
}
