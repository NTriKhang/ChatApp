package com.example.ChatApp.Models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.ChatApp.Models.Submodels.MessageGroup_User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



@Document("Users")
public class Users implements UserDetails{
	@Id
	public String _id;

    public String getDisplay_name() {
        return Display_name;
    }

    public void setDisplay_name(String display_name) {
        Display_name = display_name;
    }

    public String Display_name;
	public String Tag;
	public Date Birth;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String Email;

    public void setPassword(String password) {
        Password = password;
    }

    public String Password;
    public String Image_path;
    public String Background_image_path;
    // public List<MessageGroup_User> List_message_group;
    public List<MessageGroup_User> List_message_group;
    public Date Created_day;
    public Date Edited_day;
    public Object setEmail;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }


    @Override
    public String getUsername() {
        return Email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setEmail(Object email) {
    }
}


