package com.example.ChatApp.Models;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.example.ChatApp.Models.Submodels.File_Msg;
import com.example.ChatApp.Models.Submodels.Reply_Msg;
import com.example.ChatApp.Models.Submodels.SenderUser_Msg;

@Document("Messages")
public class Messages {
	@Id
	public String _id;
	public String Content;
	public String Media_path;
	public String Type;
	public File_Msg Attach_file;
	public String Message_group_id;
	@Field(targetType = FieldType.DATE_TIME)
	public LocalDateTime Created_date;
	public SenderUser_Msg Sender_user;

	public Reply_Msg Reply_to_msg;
	@Field(targetType = FieldType.ARRAY)
	public List<String> Seen_by = null;
	@Field(targetType = FieldType.ARRAY)
	public List<String> Unseen;
}
