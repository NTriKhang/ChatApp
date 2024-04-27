package com.example.ChatApp.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import com.example.ChatApp.Config.Utility;
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
	public ObjectId Message_group_id;
	@Field(targetType = FieldType.DATE_TIME)
	public LocalDateTime Created_date;
	public SenderUser_Msg Sender_user;

	public Reply_Msg Reply_to_msg;
	@Field(targetType = FieldType.ARRAY)
	public List<String> Seen_by = null;
	@Field(targetType = FieldType.ARRAY)
	public List<String> Unseen;

	
	public Messages() {
		
	}
	
	public Messages(String content, String message_group_id) {
		super();
		Content = content;
		Message_group_id = new ObjectId(message_group_id);
		Type = Utility.MessageType.Text;
		Created_date = LocalDateTime.now();
		Unseen = new ArrayList<String>();
		Seen_by = new ArrayList<String>();
	}

	public Messages(String content, String message_group_id,
			SenderUser_Msg sender_user) {
		super();
		Content = content;
		Type = Utility.MessageType.Text;
		Message_group_id = new ObjectId(message_group_id);
		Created_date = LocalDateTime.now();
		Sender_user = sender_user;
		Unseen = new ArrayList<String>();
		Seen_by = new ArrayList<String>();
	}
	
}
