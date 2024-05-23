package com.example.ChatApp.SocketDto;

import org.springframework.data.annotation.Id;

import com.example.ChatApp.Models.Submodels.SenderUser_Msg;

//bên front end lưu ý
public class MessageTextDto {
	public String Content;
	public String Message_group_id = "";	
	public SenderUser_Msg Sender_user;
	public String Type = "Text";
	public String Media_path = "";
}
