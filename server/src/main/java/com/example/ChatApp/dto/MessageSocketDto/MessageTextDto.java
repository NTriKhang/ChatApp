package com.example.ChatApp.dto.MessageSocketDto;

import org.springframework.data.annotation.Id;

import com.example.ChatApp.Models.Submodels.SenderUser_Msg;

public class MessageTextDto {
	public String Content;
	public String Message_group_id;
	public SenderUser_Msg Sender_user;
}
