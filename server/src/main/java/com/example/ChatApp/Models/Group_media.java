package com.example.ChatApp.Models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.example.ChatApp.Models.Submodels.File_Msg;

@Document("Group_media")
public class Group_media {
	@Id
	public String _id;
	public File_Msg Media;
	public List<String> Unseen;
	@Field(targetType = FieldType.OBJECT_ID)
	public String Message_group_id;
	@Field(targetType = FieldType.OBJECT_ID)
	public String Message_id;
}
