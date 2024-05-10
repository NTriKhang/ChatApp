package com.example.ChatApp.Models.Submodels;

import java.sql.Date;
<<<<<<< HEAD
import java.time.LocalDate;

=======
import java.time.LocalDateTime;
>>>>>>> ce60f64576769daac99414c80bc13539a27b7053
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class LastMessage_MsgGroup {
	@Field(targetType = FieldType.OBJECT_ID)
	public String message_id;
	public String content;
	public String user_name;
<<<<<<< HEAD
	public LocalDate created_date;
	
	public LastMessage_MsgGroup() {
		
	}
	
	public LastMessage_MsgGroup(String message_id, String content, String user_name, LocalDate created_date) {
=======
	public LocalDateTime created_date;	
  
	public LastMessage_MsgGroup() {
		
	}

	public LastMessage_MsgGroup(LocalDateTime cDateTime) {
		super();
		created_date = cDateTime;
	}
	public LastMessage_MsgGroup(String message_id, String content, String user_name, LocalDateTime created_date) {
>>>>>>> ce60f64576769daac99414c80bc13539a27b7053
		super();
		this.message_id = message_id;
		this.content = content;
		this.user_name = user_name;
		this.created_date = created_date;
	}
}
