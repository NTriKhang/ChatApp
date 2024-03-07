package com.example.ChatApp.Models;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.ChatApp.Models.Submodels.MessageGroup_User;



@Document("Users")
public class Users {
	@Id
	public String _id;
	public String Display_name;
	public String Tag;
	public Date Birth;
    public String Email;
    public String Password;
    public String Image_path;
    public String Background_image_path;
    // public List<MessageGroup_User> List_message_group;
    public List<MessageGroup_User> List_message_group;
    public Date Created_day;
    public Date Edited_day;

}


