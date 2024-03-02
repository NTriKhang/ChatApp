package com.example.ChatApp.Models.Submodels;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class SenderUser_Msg {
	@Field(targetType = FieldType.OBJECT_ID)
	public String user_id;
	public String user_name;
}
