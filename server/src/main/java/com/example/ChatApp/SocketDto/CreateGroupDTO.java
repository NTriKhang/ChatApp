package com.example.ChatApp.SocketDto;

import java.util.List;

import org.bson.types.ObjectId;

public class CreateGroupDTO {
		public String userCreatedId;
		public String groupName;
	 	public List<ObjectId> userList;
	 	
	    public CreateGroupDTO() {
	    }
//	    public CreateGroupDTO(String groupName, List<ObjectId> userList, 
//	    		String MsgConnectedId, String userIdCreated) {
//	        this.groupName = groupName;
//	        this.userList = userList;
//	        this.MsgConnectedId = MsgConnectedId;
//	        this.userCreatedId = userCreatedId;
//	    }
	    
}
