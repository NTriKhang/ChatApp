package com.example.ChatApp.SocketDto;

import org.springframework.data.annotation.Id;

import com.example.ChatApp.Models.Submodels.SenderUser_Msg;

//bên front end lưu ý
public class MessageTextDto {
	public String Content;
	public String Message_group_id = ""; // khi gửi tin nhắn bình thường, cần đính kém cái này, ReceiveUser thì không cần
	
	//public String ReceiveUserId = ""; // khi nhắn tin 2 người, đính kèm cái này, không cần Message_group_id
	public SenderUser_Msg Sender_user;
}
