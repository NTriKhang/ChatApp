package com.example.ChatApp.SocketDto;

import com.example.ChatApp.Models.Submodels.SenderUser_Msg;

public class MessageTextIndDto {
	public String Content;
	public String SenderId;
	public String SenderName;
	
	public String MsgGroupSenderId = ""; 
	
	public String ReceiverId = "";	
	public String Type = "Text";
	public String Media_path = "";

}
