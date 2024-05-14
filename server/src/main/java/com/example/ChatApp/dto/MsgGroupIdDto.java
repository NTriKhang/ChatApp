package com.example.ChatApp.dto;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class MsgGroupIdDto extends IdDto {
	@Field(targetType = FieldType.OBJECT_ID)
	public String messageGroupId;
}
