package com.example.ChatApp.Models.Submodels;

public class Reply_Msg {
	public String message_id;
	public String content;
	public Reply_Msg(String message_id, String content) {
		super();
		this.message_id = message_id;
		this.content = content;
	}
	public Reply_Msg() {
		super();
	}
	
}
